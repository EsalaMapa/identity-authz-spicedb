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
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit test class for {@link Permission}.
 */
@Test
public class PermissionTest {

    @Test
    public void testClassCreationWithJson() {

        String json = "{\"name\": \"permission\",\"comment\": \"comment\",\"parentDefinitionName\": \"definition\"}";

        Permission permission = new Gson().fromJson(json, Permission.class);

        assertNotNull(permission);
        assertNotNull(permission.getPermissionName());
        assertNotNull(permission.getPermissionComment());
        assertNotNull(permission.getParentDefinitionName());
        assertEquals(permission.getPermissionName(), "permission");
        assertEquals(permission.getPermissionComment(), "comment");
        assertEquals(permission.getParentDefinitionName(), "definition");
    }

    @Test
    public void testGetSubjectTypes() throws Exception {

        Permission permission = new Permission();
        Field subjectTypesField = permission.getClass().getDeclaredField("subjectTypes");
        subjectTypesField.setAccessible(true);
        subjectTypesField.set(permission, new ArrayList<>());

        assertNotNull(permission.getSubjectTypes());
        assertEquals(permission.getSubjectTypes().size(), 0);
    }

}
