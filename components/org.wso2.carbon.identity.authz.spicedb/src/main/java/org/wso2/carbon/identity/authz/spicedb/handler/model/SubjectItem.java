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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

/**
 * The {@code SubjectItem} class represents the subject object that is used in a permission check request.
 */
public class SubjectItem {

    @SerializedName(SpiceDbModelConstants.OBJECT)
    private SubjectObject subjectObject;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_RELATION)
    @Expose
    private String optionalRelation;

    public SubjectItem(String subjectType, String subjectId) {

        this.subjectObject = new SubjectObject(subjectType, subjectId);
    }

    public void setOptionalRelation(String optionalRelation) {

        this.optionalRelation = optionalRelation;
    }

    public String getSubjectType() {

        return subjectObject.subjectType;
    }

    public String getSubjectId() {

        return subjectObject.subjectId;
    }

    public String getOptionalRelation() {

        return optionalRelation;
    }

    /**
     * The {@code SubjectObject} class represents the object inside the subject in a permission check request.
     */
    private static class SubjectObject {

        @SerializedName(SpiceDbModelConstants.OBJECT_TYPE)
        private String subjectType;
        @SerializedName(SpiceDbModelConstants.OBJECT_ID)
        private String subjectId;

        public SubjectObject(String subjectType, String subjectId) {

            this.subjectType = subjectType;
            this.subjectId = subjectId;
        }
    }
}
