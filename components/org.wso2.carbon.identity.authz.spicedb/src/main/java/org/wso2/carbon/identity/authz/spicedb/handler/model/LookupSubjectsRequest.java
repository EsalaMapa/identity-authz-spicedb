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
import org.wso2.carbon.identity.authorization.framework.model.SearchSubjectsRequest;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbApiConstants;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;
import org.wso2.carbon.identity.authz.spicedb.handler.util.ConsistencyUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The {@code LookupSubjectsRequest} class represents the request body of a lookup subjects request sent to SpiceDB.
 */
@SuppressFBWarnings(value = "URF_UNREAD_FIELD",
        justification = "All fields are accessed via Gson serialization")
public class LookupSubjectsRequest {

    @SerializedName(SpiceDbModelConstants.CONSISTENCY)
    @Expose
    private Consistency consistency;
    @SerializedName(SpiceDbModelConstants.SUBJECT_OBJECT_TYPE)
    @Expose
    private String subjectType;
    @SerializedName(SpiceDbModelConstants.PERMISSION)
    @Expose
    private String permission;
    @SerializedName(SpiceDbModelConstants.RESOURCE)
    @Expose
    private Resource resource;
    @SerializedName(SpiceDbModelConstants.CONTEXT)
    @Expose
    private Map<String, Object> context;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_SUBJECT_RELATION)
    @Expose
    private String optionalSubjectRelation;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_CONCRETE_LIMIT)
    @Expose
    private long optionalConcreteLimit;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_CURSOR)
    @Expose
    private String optionalCursor;
    @SerializedName(SpiceDbModelConstants.WILD_CARD_OPTION)
    @Expose
    private String wildCardOption;

    public LookupSubjectsRequest(SearchSubjectsRequest searchSubjectsRequest) {

        if (searchSubjectsRequest.getResource() == null ||
                searchSubjectsRequest.getAction() == null ||
                searchSubjectsRequest.getSubject() == null) {
            throw new IllegalArgumentException("Invalid request. Resource, action, and subject with type " +
                    "must be provided to lookup subjects.");
        }
        if (searchSubjectsRequest.getResource().getResourceId() == null) {
            throw new IllegalArgumentException("Invalid request. Resource id must be provided to lookup subjects.");
        }
        this.consistency = ConsistencyUtil.setConsistency(SpiceDbApiConstants.ENABLE_CONSISTENCY);
        // Convert subject type and permission to lower case since SpiceDB does not allow upper case letters.
        this.subjectType = searchSubjectsRequest.getSubject().getSubjectType().toLowerCase(Locale.ROOT);
        this.permission = searchSubjectsRequest.getAction().getAction().toLowerCase(Locale.ROOT);
        this.resource = new Resource(searchSubjectsRequest.getResource().getResourceType(),
                searchSubjectsRequest.getResource().getResourceId());
    }

    public void setContext(Map<String, Object> context) {

        this.context = context != null ? new HashMap<>(context) : null;
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
}
