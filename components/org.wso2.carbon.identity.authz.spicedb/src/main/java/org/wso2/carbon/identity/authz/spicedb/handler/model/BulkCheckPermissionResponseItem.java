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
 * The {@code BulkCheckPermissionResponseItem} class represents an individual response item from a bulk
 * permission check response. This contains the request that was sent with the result or an error if occurred.
 */
public class BulkCheckPermissionResponseItem {

    @SerializedName(SpiceDbModelConstants.REQUEST)
    private Request sentRequest;
    @SerializedName(SpiceDbModelConstants.RESULT_ITEM)
    private Map<String, Object> result;
    @SerializedName(SpiceDbModelConstants.ERROR)
    private Map<String, Object> error;
    private boolean isResultAvailable;

    public String getResourceId() {

        return sentRequest.resourceItem.getResourceId();
    }

    public String getResourceType() {

        return sentRequest.resourceItem.getResourceType();
    }

    public String getPermission() {

        return sentRequest.permission;
    }

    public String getSubjectId() {

        return sentRequest.subjectItem.getSubjectId();
    }

    public String getSubjectType() {

        return sentRequest.subjectItem.getSubjectType();
    }

    public Map<String, Object> getContext() {

        return sentRequest.context;
    }

    public Map<String, Object> getResult() {

        if (isResultAvailable()) {
            return result;
        } else {
            return error;
        }
    }

    /**
     * Checks whether the result is available. This is to be used when the result is retrieved.
     *
     * @return True if the result is available, false otherwise.
     */
    public boolean isResultAvailable() {

        isResultAvailable = result != null;
        return isResultAvailable;
    }

    /**
     * The {@code Request} class represents the  returned request object that was sent in the bulk permission check
     * request.
     */
    private static class Request {

        @SerializedName(SpiceDbModelConstants.RESOURCE)
        private ResourceItem resourceItem;
        @SerializedName(SpiceDbModelConstants.SUBJECT)
        private SubjectItem subjectItem;
        @SerializedName(SpiceDbModelConstants.PERMISSION)
        private String permission;
        @SerializedName(SpiceDbModelConstants.CONTEXT)
        Map<String, Object> context;
    }
}
