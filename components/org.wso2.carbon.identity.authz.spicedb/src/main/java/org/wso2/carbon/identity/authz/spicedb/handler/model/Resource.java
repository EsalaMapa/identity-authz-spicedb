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

/**
 * The {@code Resource} class represents the resource object that is used in request bodies.
 */
public class Resource {

    @SerializedName(SpiceDbModelConstants.OBJECT_TYPE)
    private String resourceType;
    @SerializedName(SpiceDbModelConstants.OBJECT_ID)
    private String resourceId;

    public Resource(String resourceType, String resourceId) {

        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
