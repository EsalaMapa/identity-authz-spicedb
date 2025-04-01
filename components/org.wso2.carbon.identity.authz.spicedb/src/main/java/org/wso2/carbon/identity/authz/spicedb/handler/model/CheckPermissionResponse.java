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
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

import java.util.Map;

/**
 * The {@code CheckPermissionResponse} class represents a response from a permission check request sent to SpiceDB.
 */
public class CheckPermissionResponse {

    @SerializedName(SpiceDbModelConstants.CHECKED_AT)
    private ZedToken token;
    @SerializedName(SpiceDbModelConstants.PERMISSION_RESULT)
    private String permissionship;
    @SerializedName(SpiceDbModelConstants.PARTIAL_CAVEAT_INFO)
    private Map<String, Object> partialCaveatInfo;
    @SerializedName(SpiceDbModelConstants.DEBUG_TRACE)
    private Map<String, Object> debugTrace;

    public String getToken() {

        return token.getToken();
    }

    public String getPermissionship() {

        return permissionship;
    }

    public Map<String, Object> getPartialCaveatInfo() {

        return partialCaveatInfo;
    }

    public Map<String, Object> getDebugTrace() {

        return debugTrace;
    }

    /**
     * Checks whether the permission is authorized or not based on the permission result.
     * <p>
     *     If the permission result is {@link SpiceDbModelConstants#HAS_PERMISSION}, then the permission is authorized.
     *     Otherwise, if the result is {@link SpiceDbModelConstants#CONDITIONAL_PERMISSION} or
     *     {@link SpiceDbModelConstants#NO_PERMISSION} the permission is not authorized.
     *</p>
     * @return {@code true} if the permission is authorized, {@code false} otherwise.
     */
    public boolean isAuthorized() {

        return getPermissionship().equals(SpiceDbModelConstants.HAS_PERMISSION);
    }
}
