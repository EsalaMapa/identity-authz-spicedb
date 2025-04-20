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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

import java.util.Map;

/**
 * The {@code ReadRelationshipsResult} class represents the result item of a read relationships response returned by
 * SpiceDB.
 */
@SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR",
        justification = "Field is populated via Gson deserialization")
public class ReadRelationshipsResult {

    @SerializedName(SpiceDbModelConstants.READ_AT)
    @Expose
    private ZedToken readAt;
    @SerializedName(SpiceDbModelConstants.RELATIONSHIP)
    @Expose
    private Relationship relationship;
    @SerializedName(SpiceDbModelConstants.AFTER_RESULT_CURSOR)
    @Expose
    private String afterResultCursor;

    public String getReadAt () {

        return readAt.getToken();
    }

    public String getResourceId() {

        return relationship.resource.getResourceId();
    }

    public String getResourceType() {

        return relationship.resource.getResourceType();
    }

    public String getRelation () {

        return relationship.relation;
    }

    public String getSubjectId () {

        return relationship.subject.getSubjectId();
    }

    public String getSubjectType () {

        return relationship.subject.getSubjectType();
    }

    public String getOptionalRelation () {

        return relationship.subject.getOptionalRelation();
    }

    public Map<String, Object> getOptionalCaveat () {

        return relationship.optionalCaveat;
    }

    public String getAfterResultCursor () {

        return afterResultCursor;
    }
}
