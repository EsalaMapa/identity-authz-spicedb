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

import com.google.gson.annotations.SerializedName;
import org.wso2.carbon.identity.authorization.framework.model.AccessEvaluationResponse;
import org.wso2.carbon.identity.authorization.framework.model.BulkAccessEvaluationResponse;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The {@code BulkCheckPermissionResponse} class represents the response object for the bulk permission check request.
 */
public class BulkCheckPermissionResponse {

    @SerializedName(SpiceDbModelConstants.BULK_CHECK_RESULTS)
    private List<BulkCheckPermissionResponseItem> results;
    @SerializedName(SpiceDbModelConstants.CHECKED_AT)
    private CheckedAtToken token;

    public String getToken() {

        return token.getToken();
    }

    public List<BulkCheckPermissionResponseItem> getResults() {

        return this.results;
    }

    /**
     * Converts the {@code BulkCheckPermissionResponse} object to an {@code BulkAccessEvaluationResponse} object.
     *
     * @return An {@code BulkAccessEvaluationResponse} object.
     * @see BulkAccessEvaluationResponse
     */
    public BulkAccessEvaluationResponse toBulkAccessEvalResponse() {

        ArrayList<AccessEvaluationResponse> results = new ArrayList<>();
        for (BulkCheckPermissionResponseItem result : this.results) {
            if (result.isResultAvailable()) {
                String permissionship = (String) result.getResult().get(SpiceDbModelConstants.PERMISSION_RESULT);
                AccessEvaluationResponse accessEvaluationResponse = new AccessEvaluationResponse(
                        SpiceDbModelConstants.HAS_PERMISSION.equals(permissionship));
                Object partialCaveatInfo = result.getResult().get(SpiceDbModelConstants.PARTIAL_CAVEAT_INFO);
                if (partialCaveatInfo != null) {
                    accessEvaluationResponse.setContext((HashMap<String, Object>) partialCaveatInfo);
                }
                results.add(accessEvaluationResponse);
            } else {
                HashMap<String, Object> errorContext = new HashMap<>();
                errorContext.put("error", result.getResult());
                AccessEvaluationResponse accessEvaluationResponse = new AccessEvaluationResponse(false);
                accessEvaluationResponse.setContext(errorContext);
                results.add(accessEvaluationResponse);
            }
        }
        return new BulkAccessEvaluationResponse(results);
    }

    /**
     * The {@code CheckedAtToken} class represents the token object that is used to keep consistency when checking
     * permissions.
     */
    private static class CheckedAtToken {

        @SerializedName(SpiceDbModelConstants.ZED_TOKEN)
        private String token;

        public String getToken() {

            return token;
        }
    }
}
