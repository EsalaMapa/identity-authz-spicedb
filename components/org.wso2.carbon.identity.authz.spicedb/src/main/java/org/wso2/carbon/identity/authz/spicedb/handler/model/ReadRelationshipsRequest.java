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
 * The {@code ReadRelationshipsRequest} class represents the request body of a read relationships request sent to
 * SpiceDB.
 */
@SuppressFBWarnings(value = "URF_UNREAD_FIELD",
        justification = "All fields are accessed via Gson serialization/deserialization")
public class ReadRelationshipsRequest {

    @SerializedName(SpiceDbModelConstants.RELATIONSHIP_FILTER)
    @Expose
    private RelationshipFilter relationshipFilter;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_LIMIT)
    @Expose
    private Long optionalLimit;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_CURSOR)
    @Expose
    private String optionalCursor;

//TODO: (below constructor is to be used with the read data request implementation.)

//    public ReadRelationshipsRequest(ReadAuthzDataRequest readAuthzDataRequest) {
//
//        this.relationshipFilter = new RelationshipFilter(readAuthzDataRequest.getResourceType());
//        this.relationshipFilter.setSubjectType(readAuthzDataRequest.getSubjectType());
//        this.relationshipFilter.setOptionalSubjectId(readAuthzDataRequest.getSubjectId());
//        this.relationshipFilter.setOptionalRelation(readAuthzDataRequest.getAction());
//        this.relationshipFilter.setOptionalResourceId(readAuthzDataRequest.getResourceId());
//        if (readAuthzDataRequest.getContext() != null) {
//            HashMap<String, Object> context = readAuthzDataRequest.getContext();
//            if (context.containsKey(SpiceDbModelConstants.OPTIONAL_RESOURCE_ID_PREFIX)) {
//                this.relationshipFilter.setOptionalResourceIdPrefix((String) context.get(SpiceDbModelConstants
//                        .OPTIONAL_RESOURCE_ID_PREFIX));
//            }
//            if (context.containsKey(SpiceDbModelConstants.OPTIONAL_SUBJECT_RELATION)) {
//                this.relationshipFilter.setOptionalSubjectRelation((String) context.get(SpiceDbModelConstants
//                        .OPTIONAL_SUBJECT_RELATION));
//            }
//        }
//    }

    //This constructor is used in Search Actions flow. Read relationships flow is used as a workaround since SpiceDB
    //does not have an endpoint to search actions yet.
    public ReadRelationshipsRequest(Resource resource, Subject subject) {

        this.relationshipFilter = new RelationshipFilter(resource.getResourceType());
        this.relationshipFilter.setOptionalResourceId(resource.getResourceId());
        OptionalSubjectFilter optionalSubjectFilter = new OptionalSubjectFilter(subject.getSubjectType());
        optionalSubjectFilter.setOptionalSubjectId(subject.getSubjectId());
        this.relationshipFilter.setOptionalSubjectFilter(optionalSubjectFilter);

    }

    public void setOptionalLimit (Long optionalLimit) {

        this.optionalLimit = optionalLimit;
    }

    public void setOptionalCursor (String optionalCursor) {

        this.optionalCursor = optionalCursor;
    }
}
