/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.authz.spicedb.handler.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbConstants;
import org.wso2.carbon.identity.oauth2.fga.models.AuthzBulkCheckResponse;
import org.wso2.carbon.identity.oauth2.fga.models.AuthzCheckRequest;
import org.wso2.carbon.identity.oauth2.fga.models.AuthzCheckResponse;
import org.wso2.carbon.identity.oauth2.fga.models.ErrorResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The {@code PermissionBulkCheckResponse} class represents the response object for the bulk permission check request.
 */
public class PermissionBulkCheckResponse {

    private List<PermissionBulkCheckResponseObject> results;
    private String token;

    private static final String CHECKED_AT_TOKEN = "checkedAt";
    private static final String TOKEN = "token";
    private static final String RESULTS = "pairs";
    private static final String PERMISSION = "permissionship";

    public PermissionBulkCheckResponse(JSONObject object) {

        if (object.has(CHECKED_AT_TOKEN)) {
            this.token = object.getJSONObject(CHECKED_AT_TOKEN).getString(TOKEN);
            this.results = new ArrayList<>();
            JSONArray pairsArray = object.getJSONArray(RESULTS);
            for (int i = 0; i < pairsArray.length(); i++) {
                JSONObject pair = pairsArray.getJSONObject(i);
                results.add(new PermissionBulkCheckResponseObject(pair));
            }
        }
    }

    public String getToken() {

        return token;
    }

    public List<PermissionBulkCheckResponseObject> getResults() {

        return this.results;
    }

    /**
     * Converts the {@code PermissionBulkCheckResponse} object to an {@code AuthzBulkCheckResponse} object.
     *
     * @return An {@code AuthzBulkCheckResponse} object.
     */
    public AuthzBulkCheckResponse toAuthzBulkCheckResponse() {

        HashMap<AuthzCheckRequest, AuthzCheckResponse> results = new HashMap<>();
        HashMap<AuthzCheckRequest, ErrorResponse> errorResults = new HashMap<>();
        for (PermissionBulkCheckResponseObject result : this.results) {
            if (result.isResultAvailable()) {
                String permissionship = result.getResult().getString(PERMISSION);
                AuthzCheckResponse authzCheckResponse = new AuthzCheckResponse(permissionship
                        .equals(SpiceDbConstants.HAS_PERMISSION));
                Object partialCaveatInfo = result.getResult().get("partialCaveatInfo");
                if (partialCaveatInfo != null) {
                    authzCheckResponse.setAdditionalContext
                            (!JSONObject.NULL.equals(result.getResult().get("partialCaveatInfo")) ?
                            result.getResult().getJSONObject("partialCaveatInfo") : null);
                }
                AuthzCheckRequest key = result.toAuthzCheckRequest();
                results.put(key, authzCheckResponse);
            } else {
                SpiceDbErrorResponse spiceDbErrorResponse = new SpiceDbErrorResponse(result.getResult());
                ErrorResponse errorResponse = new ErrorResponse(spiceDbErrorResponse.getMessage());
                errorResponse.setErrorCode(spiceDbErrorResponse.getCode());
                errorResponse.setErrorDetails(spiceDbErrorResponse.getDetails());
                AuthzCheckRequest key = result.toAuthzCheckRequest();
                errorResults.put(key, errorResponse);
            }
        }
        return new AuthzBulkCheckResponse(results, errorResults);
    }
}
