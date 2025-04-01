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
import org.wso2.carbon.identity.authorization.framework.model.AccessEvaluationRequest;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

import java.util.ArrayList;

/**
 * The {@code BulkCheckPermissionRequest} class is a model class for the bulk permission check request body.
 */
public class BulkCheckPermissionRequest {

    @SerializedName(SpiceDbModelConstants.BULK_CHECK_REQUESTS)
    private ArrayList<CheckPermissionRequest> items;

    public BulkCheckPermissionRequest(ArrayList<AccessEvaluationRequest> items) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Invalid request. The list of requests cannot be null or empty.");
        }
        this.items = new ArrayList<>();
        for (AccessEvaluationRequest item : items) {
            this.items.add(new CheckPermissionRequest(item));
        }
    }
}
