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

/**
 * The {@code RelationshipFilter} class represents the relationship filter object in read and delete requests sent
 * to SpiceDB.
 */
@SuppressFBWarnings(value = "URF_UNREAD_FIELD",
        justification = "All fields are accessed via Gson serialization/deserialization")
public class RelationshipFilter {

    @SerializedName(SpiceDbModelConstants.RESOURCE_TYPE)
    @Expose
    private String resourceType;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_RESOURCE_ID)
    @Expose
    private String optionalResourceId;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_RESOURCE_ID_PREFIX)
    @Expose
    private String optionalResourceIdPrefix;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_RELATION)
    @Expose
    private String optionalRelation;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_SUBJECT_FILTER)
    @Expose
    private OptionalSubjectFilter optionalSubjectFilter;

    public RelationshipFilter(String resourceType) {

        this.resourceType = resourceType;
    }

    public void setOptionalResourceId (String optionalResourceId) {

        this.optionalResourceId = optionalResourceId;
    }

    public void setOptionalResourceIdPrefix (String optionalResourceIdPrefix) {

        this.optionalResourceIdPrefix = optionalResourceIdPrefix;
    }

    public void setOptionalRelation (String optionalRelation) {

        this.optionalRelation = optionalRelation;
    }

    public void setOptionalSubjectFilter(OptionalSubjectFilter optionalSubjectFilter) {

        this.optionalSubjectFilter = optionalSubjectFilter;
    }
}
