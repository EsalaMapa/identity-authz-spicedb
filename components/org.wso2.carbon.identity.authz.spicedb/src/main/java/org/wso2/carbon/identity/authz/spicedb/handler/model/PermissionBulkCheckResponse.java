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
import org.wso2.carbon.identity.oauth2.fga.models.AuthzCheckResponse;
import org.wso2.carbon.identity.oauth2.fga.models.ErrorResponse;

import java.util.HashMap;

/**
 * This class is a model class to receive permission bulk check responses from SpiceDB.
 */
public class PermissionBulkCheckResponse {

    private HashMap<String, JSONObject> results;
    private HashMap<String, JSONObject> errorResults;
    private String token;

    private static final String CHECKED_AT_TOKEN = "checkedAt";
    private static final String TOKEN = "token";
    private static final String RESULTS = "pairs";
    private static final String REQUEST = "request";
    private static final String RESOURCE = "resource";
    private static final String RESULT_ITEM = "item";
    private static final String RESOURCE_ID = "objectId";
    private static final String PERMISSION = "permissionship";
    private static final String ERROR = "error";

    public PermissionBulkCheckResponse(JSONObject object) {

        if (object.has(CHECKED_AT_TOKEN)) {
            this.token = object.getJSONObject(CHECKED_AT_TOKEN).getString(TOKEN);
            this.results = new HashMap<>();
            this.errorResults = new HashMap<>();
            JSONArray pairsArray = object.getJSONArray(RESULTS);
            for (int i = 0; i < pairsArray.length(); i++) {
                JSONObject resource = pairsArray.getJSONObject(i).getJSONObject(REQUEST).getJSONObject(RESOURCE);
                if (pairsArray.getJSONObject(i).has(RESULT_ITEM)) {
                    JSONObject item = pairsArray.getJSONObject(i).getJSONObject(RESULT_ITEM);
                    this.results.put(resource.getString(RESOURCE_ID), item);
                } else if (pairsArray.getJSONObject(i).has(ERROR)) {
                    JSONObject error = pairsArray.getJSONObject(i).getJSONObject(ERROR);
                    this.errorResults.put(resource.getString(RESOURCE_ID), error);
                }
            }
        }
    }

    public String getToken() {

        return token;
    }

    public HashMap<String, JSONObject> getResults() {

        return this.results;
    }

    public HashMap<String, JSONObject> getErrorResults() {

        return this.errorResults;
    }

    public AuthzBulkCheckResponse toAuthzBulkCheckResponse() {

        HashMap<String, AuthzCheckResponse> results = new HashMap<>();
        HashMap<String, ErrorResponse> errorResults = new HashMap<>();
        for (String key : this.results.keySet()) {
            String permissionship = this.results.get(key).getString(PERMISSION);
            AuthzCheckResponse authzCheckResponse = new AuthzCheckResponse(permissionship
                    .equals(SpiceDbConstants.HAS_PERMISSION));
            results.put(key, authzCheckResponse);
        }
        for (String key : this.errorResults.keySet()) {
            SpiceDbErrorResponse spiceDbErrorResponse = new SpiceDbErrorResponse(this.errorResults.get(key));
            ErrorResponse errorResponse = new ErrorResponse(spiceDbErrorResponse.getMessage());
            errorResponse.setErrorCode(spiceDbErrorResponse.getCode());
            errorResponse.setErrorDetails(spiceDbErrorResponse.getDetails());
            errorResults.put(key, errorResponse);
        }
        return new AuthzBulkCheckResponse(results, errorResults);
    }
}
