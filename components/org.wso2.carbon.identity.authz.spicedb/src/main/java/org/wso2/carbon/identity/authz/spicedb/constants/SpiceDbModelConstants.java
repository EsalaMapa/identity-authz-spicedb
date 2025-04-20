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

package org.wso2.carbon.identity.authz.spicedb.constants;

/**
 * The {@code SpiceDbModelConstants} class contains the constant values used in request and response models.
 */
public class SpiceDbModelConstants {

    //Permission response values.
    //Permission response if the permission is granted.
    public static final String HAS_PERMISSION = "PERMISSIONSHIP_HAS_PERMISSION";
    //Permission response if the permission is denied.
    public static final String NO_PERMISSION = "PERMISSIONSHIP_NO_PERMISSION";
    //Permission response if the permission is conditional because of missing context.
    public static final String CONDITIONAL_PERMISSION = "PERMISSIONSHIP_CONDITIONAL_PERMISSION";

    //General response values.
    public static final String CHECKED_AT = "checkedAt";
    public static final String ZED_TOKEN = "token";
    public static final String PERMISSION_RESULT = "permissionship";

    //Check request values.
    public static final String PARTIAL_CAVEAT_INFO = "partialCaveatInfo";
    public static final String DEBUG_TRACE = "debugTrace";

    //Check response values.
    public static final String REQUEST = "request";
    public static final String RESOURCE = "resource";
    public static final String SUBJECT = "subject";
    public static final String RESULT_ITEM = "item";
    public static final String PERMISSION = "permission";
    public static final String CONTEXT = "context";
    public static final String ERROR = "error";
    public static final String WITH_TRACING = "withTracing";

    // Subject and Resource Object values.
    public static final String OBJECT = "object";
    public static final String OBJECT_TYPE = "objectType";
    public static final String OBJECT_ID = "objectId";
    public static final String OPTIONAL_RELATION = "optionalRelation";

    //Bulk check request and response values.
    public static final String BULK_CHECK_REQUESTS = "items";
    public static final String BULK_CHECK_RESULTS = "pairs";

    //Error response values.
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String DETAILS = "details";

    //Lookup request and response values
    public static final String LOOKED_AT = "lookedAt";
    public static final String RESOURCE_ID = "resourceId";
    public static final String AFTER_RESULT_CURSOR = "afterResultCursor";
    public static final String OPTIONAL_LIMIT = "optionalLimit";
    public static final String OPTIONAL_SUBJECT_RELATION = "optionalSubjectRelation";
    public static final String RESULT = "result";
    public static final String RESOURCE_OBJECT_TYPE = "resourceObjectType";
    public static final String OPTIONAL_CURSOR = "optionalCursor";
    public static final String OPTIONAL_CONCRETE_LIMIT = "optionalConcreteLimit";
    public static final String WILD_CARD_OPTION = "wildCardOption";
    public static final String SUBJECT_ID = "subjectObjectId";
    public static final String SUBJECT_OBJECT_TYPE = "subjectObjectType";

    //Data read and update request and response values
    public static final String READ_AT = "readAt";
    public static final String RELATIONSHIP_FILTER = "relationshipFilter";
    public static final String OPTIONAL_RESOURCE_ID_PREFIX = "optionalResourceIdPrefix";
    public static final String SUBJECT_TYPE = "subjectType";
    public static final String RELATIONSHIP = "relationship";
    public static final String OPTIONAL_SUBJECT_ID = "optionalSubjectId";
    public static final String RELATION = "relation";
    public static final String OPTIONAL_CAVEAT = "optionalCaveat";
    public static final String RESOURCE_TYPE = "resourceType";
    public static final String OPTIONAL_RESOURCE_ID = "optionalResourceId";
    public static final String OPTIONAL_SUBJECT_FILTER = "optionalSubjectFilter";
}
