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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.wso2.carbon.identity.authorization.framework.model.AccessEvaluationRequest;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

import java.util.HashMap;

/**
 * The {@code CheckPermissionRequest} class represents the request body of a permission check request sent to SpiceDB.
 */
public class CheckPermissionRequest {

    @SerializedName(SpiceDbModelConstants.RESOURCE)
    private ResourceItem resourceItem;
    @SerializedName(SpiceDbModelConstants.PERMISSION)
    private String permission;
    @SerializedName(SpiceDbModelConstants.SUBJECT)
    private SubjectItem subjectItem;
    @SerializedName(SpiceDbModelConstants.CONTEXT)
    @Expose
    private HashMap<String, Object> context;
    @SerializedName(SpiceDbModelConstants.WITH_TRACING)
    @Expose
    private boolean withTracing;

    public CheckPermissionRequest(AccessEvaluationRequest accessEvaluationRequest) {

        this.resourceItem = new ResourceItem(accessEvaluationRequest.getResource().getResourceType(),
                accessEvaluationRequest.getResource().getResourceId());
        this.permission = accessEvaluationRequest.getActionObject().getAction();
        this.subjectItem = new SubjectItem(accessEvaluationRequest.getSubject().getSubjectType(),
                accessEvaluationRequest.getSubject().getSubjectId());
        if (accessEvaluationRequest.getContext() != null) {
            if (accessEvaluationRequest.getContext().containsKey(SpiceDbModelConstants.CONTEXT)) {
                this.context = (HashMap<String, Object>) accessEvaluationRequest.getContext()
                        .get(SpiceDbModelConstants.CONTEXT);
            }
            if (accessEvaluationRequest.getContext().containsKey(SpiceDbModelConstants.WITH_TRACING)) {
                this.withTracing = (boolean) accessEvaluationRequest.getContext()
                        .get(SpiceDbModelConstants.WITH_TRACING);
            }
        }
    }

    public void setWithTracing (boolean withTracing) {

        this.withTracing = withTracing;
    }

    public void setContext(HashMap<String, Object> context) {

        this.context = context;
    }

    /**
     * Converts the {@code CheckPermissionRequest} object to a JSON string.
     *
     * @return A JSON string.
     */
    public String parseToJsonString() {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this, CheckPermissionRequest.class);
    }

}
