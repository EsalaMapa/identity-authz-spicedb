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
public class LookupResourcesRequest {

    private String resourceObjectType;
    private String permission;
    private String subjectObjectType;
    private String subjectObjectId;
    private String optionalRelation;
    private long optionalLimit;
    private String optionalCursor;
    private JSONObject context;

    public LookupResourcesRequest (ListObjectsRequest listObjectsRequest) {

        this.resourceObjectType = listObjectsRequest.getResultObjectType();
        this.permission = listObjectsRequest.getRelation();
        this.subjectObjectType = listObjectsRequest.getSearchObjectType();
        this.subjectObjectId = listObjectsRequest.getSearchObjectId();
    }

    public void setContext (JSONObject context) {

        this.context = context;
    }

    public void setOptionalRelation (String optionalRelation) {

        this.optionalRelation = optionalRelation;
    }

    public void setOptionalLimit (Long optionalLimit) {

        this.optionalLimit = optionalLimit;
    }

    public void setOptionalCursor (String optionalCursor) {

        this.optionalCursor = optionalCursor;
    }

    public JSONObject parseToJSON () {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resourceObjectType", this.resourceObjectType);
        jsonObject.put("permission", this.permission);

        HashMap<String, Object> subject = new HashMap<>();
        HashMap<String, Object> subjectObject = new HashMap<>();
        subjectObject.put("objectType", this.subjectObjectType);
        subjectObject.put("objectId", this.subjectObjectId);
        subjectObject.put("optionalRelation", optionalRelation);
        subject.put("object", subjectObject);
        jsonObject.put("subject", subject);

        jsonObject.put("context", this.context);
        jsonObject.put("optionalLimit", optionalLimit);
        if (optionalCursor != null) {
            jsonObject.put("optionalCursor", new HashMap<>().put("token", optionalCursor));
        }
        return jsonObject;
    }
}
