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

package org.wso2.carbon.identity.authz.spicedb.handler.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.authorization.framework.model.AccessEvaluationRequest;
import org.wso2.carbon.identity.authorization.framework.model.AuthorizationResource;
import org.wso2.carbon.identity.authorization.framework.model.AuthorizationSubject;
import org.wso2.carbon.identity.authz.spicedb.internal.SpiceDbAuthzServiceComponent;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.common.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class for modifying the request to be sent in interoperability demo with AuthZEN.
 */
public class AuthzenInteropRequestUtil {

    private static final String IDENTITY_ID_CLAIM_URI = "http://wso2.org/claims/IdentityId";
    private static final String EMAIL_CLAIM_URI = "http://wso2.org/claims/emailaddress";

    private static final Log LOG = LogFactory.getLog(AuthzenInteropRequestUtil.class);

    public static AccessEvaluationRequest modifyRequest(AccessEvaluationRequest accessEvaluationRequest)
            throws Exception {

        if (accessEvaluationRequest.getResource() == null ||
                accessEvaluationRequest.getActionObject() == null ||
                accessEvaluationRequest.getSubject() == null) {
            throw new IllegalArgumentException("Invalid request. Resource, action, and subject " +
                    "must be provided for permission check.");
        }
        if (accessEvaluationRequest.getResource().getResourceId() == null ||
                accessEvaluationRequest.getSubject().getSubjectId() == null) {
            throw new IllegalArgumentException("Invalid request. Resource Id and subject Id " +
                    "must be provided for permission check.");
        }
        if ("identity".equals(accessEvaluationRequest.getSubject().getSubjectType())) {
            return getGatewayRequest(accessEvaluationRequest);
        } else if ("todo".equals(accessEvaluationRequest.getResource().getResourceType()) ||
        "user".equals(accessEvaluationRequest.getResource().getResourceType())) {
            return getFgaRequest(accessEvaluationRequest);
            
        } else {
            return accessEvaluationRequest;
        }
    }

    private static AccessEvaluationRequest getGatewayRequest(AccessEvaluationRequest accessEvaluationRequest) {

        AuthorizationSubject authorizationSubject = new AuthorizationSubject("user", accessEvaluationRequest
                .getSubject().getSubjectId());
        String resourceId = accessEvaluationRequest.getResource().getResourceId().split("/")[1];
        if ("users".equals(resourceId)) {
            AuthorizationResource authorizationResource = new AuthorizationResource("route_user", resourceId);
            return new AccessEvaluationRequest(authorizationSubject, accessEvaluationRequest.getActionObject(),
                    authorizationResource);
        } else if ("todos".equals(resourceId)) {
            AuthorizationResource authorizationResource = new AuthorizationResource("route_todo", resourceId);
            return new AccessEvaluationRequest(authorizationSubject, accessEvaluationRequest.getActionObject(),
                    authorizationResource);
        }
        return accessEvaluationRequest;
    }

    private static AccessEvaluationRequest getFgaRequest(AccessEvaluationRequest accessEvaluationRequest)
            throws Exception {

        AuthorizationResource authorizationResource;
        if ("user".equals(accessEvaluationRequest.getResource().getResourceType())) {
            authorizationResource = new AuthorizationResource("citadel", "citadel");
        } else {
            authorizationResource = new AuthorizationResource(
                    accessEvaluationRequest.getResource().getResourceType(), "todo_1");
        }
        AccessEvaluationRequest modifiedRequest = new AccessEvaluationRequest(accessEvaluationRequest.getSubject(),
                accessEvaluationRequest.getActionObject(), authorizationResource);
        if (accessEvaluationRequest.getResource().getProperties() != null) {
            if (accessEvaluationRequest.getResource().getProperties().containsKey("ownerID")) {
                String email = getEmailClaim(accessEvaluationRequest.getSubject());
                HashMap<String, Object> properties = new HashMap<>();
                properties.put("user_email", email);
                properties.put("owner_email", accessEvaluationRequest.getResource().getProperties().get("ownerID"));
                Map<String, Object> context = new HashMap<>();
                context.put("context", properties);
                modifiedRequest.setContext(context);
            }
        }

        return modifiedRequest;
    }

    private static String getEmailClaim(AuthorizationSubject authorizationSubject) throws Exception {

        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        UserRealm userRealm = (UserRealm) SpiceDbAuthzServiceComponent.getRealmservice().
                getTenantUserRealm(tenantId);
        AbstractUserStoreManager userStoreManager = (AbstractUserStoreManager) userRealm.getUserStoreManager();
        String[] usernames = userStoreManager.getUserList(IDENTITY_ID_CLAIM_URI,
                authorizationSubject.getSubjectId(), null);
        if (usernames == null || usernames.length == 0) {
            LOG.error("User is not found for given id : " + authorizationSubject.getSubjectId());
            throw new Exception();
        }
        User user = userStoreManager.getUser(null, usernames[0]);
        return userStoreManager.getUserClaimValue(user.getUsername(), EMAIL_CLAIM_URI, null);
    }
}
