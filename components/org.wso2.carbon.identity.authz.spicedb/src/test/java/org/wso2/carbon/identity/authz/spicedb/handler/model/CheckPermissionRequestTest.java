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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.authorization.framework.model.AccessEvaluationRequest;
import org.wso2.carbon.identity.authorization.framework.model.AuthorizationAction;
import org.wso2.carbon.identity.authorization.framework.model.AuthorizationResource;
import org.wso2.carbon.identity.authorization.framework.model.AuthorizationSubject;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * Unit test class for {@link CheckPermissionRequest}.
 */
@Test
public class CheckPermissionRequestTest {

    @Test
    public void testConstructorWithNullRequest() {

        try {
            new CheckPermissionRequest(new AccessEvaluationRequest(null, null, null));
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid request. Resource, action, and subject " +
                    "must be provided for permission check.");
        }
    }

    @Test
    public void testConstructorWithValidRequest() {

        AccessEvaluationRequest accessEvaluationRequest = new AccessEvaluationRequest(
                new AuthorizationSubject("subjectType", "subjectId"),
                        new AuthorizationAction("action"),
                new AuthorizationResource("resourceType", "resourceId"));
        Map<String, Object> context = new HashMap<>();
        context.put("context", new HashMap<>());
        context.put("withTracing", true);
        accessEvaluationRequest.setContext(context);
        CheckPermissionRequest checkPermissionRequest = new CheckPermissionRequest(accessEvaluationRequest);

        Assert.assertNotNull(checkPermissionRequest);
        Assert.assertNotNull(checkPermissionRequest.getResource());
        Assert.assertNotNull(checkPermissionRequest.getPermission());
        Assert.assertNotNull(checkPermissionRequest.getSubject());
        Assert.assertNotNull(checkPermissionRequest.getContext());
        Assert.assertTrue(checkPermissionRequest.isWithTracing());
    }

    @Test
    public void testConstructorWithInvalidContext() {

        AccessEvaluationRequest accessEvaluationRequest = new AccessEvaluationRequest(
                mock(AuthorizationSubject.class),
                mock(AuthorizationAction.class),
                mock(AuthorizationResource.class)
                );
        Map<String, Object> context = new HashMap<>();
        context.put("context", "invalidContext");
        accessEvaluationRequest.setContext(context);

        try {
            new CheckPermissionRequest(accessEvaluationRequest);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Invalid request. Context must be a map.");
        }
    }
}
