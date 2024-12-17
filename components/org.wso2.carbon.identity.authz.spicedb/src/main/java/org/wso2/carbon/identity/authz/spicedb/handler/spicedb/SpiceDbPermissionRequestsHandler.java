/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.authz.spicedb.handler.spicedb;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbConstants;
import org.wso2.carbon.identity.authz.spicedb.handler.model.ErrorResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.PermissionBulkCheckResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.PermissionCheckResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.util.SpiceDbHttpHandler;
import org.wso2.carbon.identity.authz.spicedb.handler.util.SpiceDbRequestInterface;
import org.wso2.carbon.identity.authz.spicedb.handler.util.SpiceDbResponseInterface;

/**
 * This class contains methods that handle  all authorization related requests from SpiceDB.
 */
public class SpiceDbPermissionRequestsHandler {

    private static final SpiceDbHttpHandler SPICEDB_HTTP_HANDLER = new SpiceDbHttpHandler();

    public SpiceDbResponseInterface checkAuthorization (SpiceDbRequestInterface authorizationRequest) {

        JSONObject object;
        try {
            HttpResponse response = SPICEDB_HTTP_HANDLER.sendPOSTRequest(SpiceDbConstants.BASE_URL +
                            SpiceDbConstants.PERMISSION_CHECK, authorizationRequest.parseToJSON());
            object = new JSONObject(SPICEDB_HTTP_HANDLER.parseToString(response));
            int statusCodeType = response.getStatusLine().getStatusCode() / 100;
            switch (statusCodeType) {
                case 2:
                case 3:
                    return new PermissionCheckResponse(object);
                case 4:
                case 5:
                    return new ErrorResponse(object);
                default:
                    return new ErrorResponse(new JSONObject("{\n\"message\": \"Cannot identify error code.\"\n\"" +
                            "details\": \"Cannot provide details. Please check spiceDB connection.\""));
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to check permission from spiceDB.", e);
        }
    }

    public SpiceDbResponseInterface bulkCheckAuthorization (SpiceDbRequestInterface bulkAuthorizationRequest) {

        JSONObject object;
        try {
            HttpResponse response = SPICEDB_HTTP_HANDLER.sendPOSTRequest(SpiceDbConstants.BASE_URL +
                    SpiceDbConstants.PERMISSIONS_BULKCHECK, bulkAuthorizationRequest.parseToJSON());
            object = new JSONObject(SPICEDB_HTTP_HANDLER.parseToString(response));
            int statusCodeType = response.getStatusLine().getStatusCode() / 100;
            switch (statusCodeType) {
                case 2:
                case 3:
                    return new PermissionBulkCheckResponse(object);
                case 4:
                case 5:
                    return new ErrorResponse(object);
                default:
                    return new ErrorResponse(new JSONObject("{\n\"message\": \"Cannot identify error code.\"\n\"" +
                            "details\": \"Cannot provide details. Please check spiceDB connection.\""));
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to bulk check permission from spiceDB.", e);
        }
    }
}
