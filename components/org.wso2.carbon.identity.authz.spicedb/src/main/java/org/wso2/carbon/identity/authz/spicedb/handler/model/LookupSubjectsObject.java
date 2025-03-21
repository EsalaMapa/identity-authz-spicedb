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

/**
 * The {@code LookupSubjectsObject} class represents the lookup subjects object that is returned from a Lookup subject
 * request.
 */
public class LookupSubjectsObject {

    private static final String RESULT = "result";
    private static final String LOOKED_UP_AT = "lookedUpAt";
    private static final String TOKEN = "token";
    private static final String SUBJECT_OBJECT_ID = "subjectObjectId";
    private static final String PERMISSIONSHIP = "permissionship";
    private static final String PARTIAL_CAVEAT_INFO = "partialCaveatInfo";
    private static final String AFTER_RESULT_CURSOR = "afterResultCursor";
    private static final String CONDITIONAL_PERMISSION = "LOOKUP_PERMISSIONSHIP_CONDITIONAL_PERMISSION";

    private String lookedAt;
    private String subjectId;
    private String permissionship;
    private JSONObject partialCaveatInfo;
    private String afterResultCursor;

    public LookupSubjectsObject (JSONObject object) {

        JSONObject resultObject = object.getJSONObject(RESULT);
        this.lookedAt = resultObject.getJSONObject(LOOKED_UP_AT).getString(TOKEN);
        this.subjectId = resultObject.getString(SUBJECT_OBJECT_ID);

        this.permissionship = resultObject.getString(PERMISSIONSHIP);
        if (permissionship.equals(CONDITIONAL_PERMISSION)) {
            this.partialCaveatInfo = resultObject.getJSONObject(PARTIAL_CAVEAT_INFO);
        }
        this.afterResultCursor = resultObject.getJSONObject(AFTER_RESULT_CURSOR).getString(TOKEN);
    }

    public String getLookedAt() {

        return lookedAt;
    }

    public String getSubjectId() {

        return subjectId;
    }

    public String getPermissionship() {

        return permissionship;
    }

    public JSONObject getPartialCaveatInfo() {

        return partialCaveatInfo;
    }

    public String getAfterResultCursor() {

        return afterResultCursor;
    }
}
