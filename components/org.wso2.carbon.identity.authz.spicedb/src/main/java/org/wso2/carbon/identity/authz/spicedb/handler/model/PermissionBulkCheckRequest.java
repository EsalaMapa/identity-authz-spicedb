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

import org.json.JSONObject;
import org.wso2.carbon.identity.oauth2.fga.models.AuthzCheckRequest;

import java.util.ArrayList;

/**
 * The {@code PermissionBulkCheckRequest} class is a model class for the bulk permission check request body.
 */
public class PermissionBulkCheckRequest {

    private ArrayList<PermissionCheckRequest> items;

    public PermissionBulkCheckRequest(ArrayList<AuthzCheckRequest> items) {

        this.items = new ArrayList<>();
        for (AuthzCheckRequest item : items) {
            this.items.add(new PermissionCheckRequest(item));
        }
    }

    public JSONObject parseToJSON() {

        JSONObject jsonObject = new JSONObject();
        JSONObject[] itemList = new JSONObject[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemList[i] = items.get(i).parseToJSON();
        }
        jsonObject.put("items", itemList);
        return jsonObject;
    }
}
