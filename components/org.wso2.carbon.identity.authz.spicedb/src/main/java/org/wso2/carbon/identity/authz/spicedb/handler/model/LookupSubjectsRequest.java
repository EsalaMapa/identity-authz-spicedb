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
import org.wso2.carbon.identity.oauth2.fga.models.ListObjectsRequest;

import java.util.HashMap;

/**
 *
 */
public class LookupSubjectsRequest {

    private String subjectType;
    private String permission;
    private String resourceType;
    private String resourceId;
    private JSONObject context;
    private String optionalSubjectRelation;
    private long optionalConcreteLimit;
    private String optionalCursor;
    private String wildCardOption;

    public LookupSubjectsRequest (ListObjectsRequest listObjectsRequest) {

        this.subjectType = listObjectsRequest.getResultObjectType();
        this.permission = listObjectsRequest.getRelation();
        this.resourceType = listObjectsRequest.getSearchObjectType();
        this.resourceId = listObjectsRequest.getSearchObjectId();
    }

    public void setContext (JSONObject context) {

        this.context = context;
    }

    public void setOptionalSubjectRelation (String optionalSubjectRelation) {

        this.optionalSubjectRelation = optionalSubjectRelation;
    }

    public void setOptionalConcreteLimit (Long optionalConcreteLimit) {

        this.optionalConcreteLimit = optionalConcreteLimit;
    }

    public void setOptionalCursor (String optionalCursor) {

        this.optionalCursor = optionalCursor;
    }

    public void setWildCardOption (String wildCardOption) {

        this.wildCardOption = wildCardOption;
    }

    public JSONObject parseToJSON() {

        JSONObject jsonObject = new JSONObject();
        HashMap<String , String> resource = new HashMap<>();
        resource.put("objectType", this.resourceType);
        resource.put("objectId", this.resourceId);
        jsonObject.put("resource", resource);
        jsonObject.put("permission", this.permission);
        jsonObject.put("subjectObjectType", this.subjectType);
        jsonObject.put("optionalSubjectRelation", optionalSubjectRelation);
        jsonObject.put("context", this.context);
        jsonObject.put("optionalConcreteLimit", optionalConcreteLimit);
        if (optionalCursor != null) {
            jsonObject.put("optionalCursor", new HashMap<>().put("token", optionalCursor));
        }
        if (wildCardOption != null) {
            jsonObject.put("wildCardOption", wildCardOption);
        }
        return jsonObject;
    }
}
