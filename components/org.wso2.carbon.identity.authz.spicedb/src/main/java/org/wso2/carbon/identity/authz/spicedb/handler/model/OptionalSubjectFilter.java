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
 * The {@code OptionalSubjectFilter} class represents the optional subject filter object in the relationship filter
 * object.
 */
@SuppressFBWarnings(value = "URF_UNREAD_FIELD",
        justification = "All fields are accessed via Gson serialization/deserialization")
public class OptionalSubjectFilter {

    @SerializedName(SpiceDbModelConstants.SUBJECT_TYPE)
    @Expose
    private String subjectType;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_SUBJECT_ID)
    @Expose
    private String optionalSubjectId;

//TODO: (To be used in Read Relationships flow.)

//    @SerializedName(SpiceDbModelConstants.OPTIONAL_RELATION)
//    @Expose
//    private OptionalSubjectRelation optionalSubjectRelation;

    OptionalSubjectFilter(String subjectType) {

        this.subjectType = subjectType;
    }

    public void setOptionalSubjectId(String optionalSubjectId) {

        this.optionalSubjectId = optionalSubjectId;
    }
}
