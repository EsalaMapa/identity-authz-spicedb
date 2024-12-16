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

package org.wso2.carbon.identity.authz.spicedb.handler.model;

import org.json.JSONObject;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbConstants;
import org.wso2.carbon.identity.authz.spicedb.handler.util.SpiceDbResponseInterface;

/**
 * This class is a model class to receive permission check responses from SpiceDB.
 */
public class PermissionCheckResponse implements SpiceDbResponseInterface {
    private String token;
    private String permissionship;
    private JSONObject partialCaveatInfo;
    private Object debugTrace;

    public PermissionCheckResponse(JSONObject response) {
        if (response.has("checkedAt")) {
            JSONObject checkedAt = response.getJSONObject("checkedAt");
            this.token = checkedAt.getString("token");
            this.permissionship = response.getString("permissionship");
            if (this.permissionship.equals(SpiceDbConstants.CONDITIONAL_PERMISSION)) {
                this.partialCaveatInfo = response.getJSONObject("partialCaveatinfo");
            }
        }
        if (response.has("debugTrace") && response.get("debugTrace") != null) {
            this.debugTrace = response.getJSONObject("debugTrace");
        }
    }

    public String getToken() {

        return token;
    }

    public String getPermissionship() {

        return permissionship;
    }

    public JSONObject getPartialCaveatInfo() {

        return partialCaveatInfo;
    }

    public Object getDebugTrace() {

        return debugTrace;
    }
}