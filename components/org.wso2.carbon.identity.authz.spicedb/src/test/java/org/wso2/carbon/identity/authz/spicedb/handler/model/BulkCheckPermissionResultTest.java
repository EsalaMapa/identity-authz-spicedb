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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test class for {@link BulkCheckPermissionResult}.
 */
@Test
public class BulkCheckPermissionResultTest {

    @Test
    public void testClassCreationWithJson() {

        String jsonString = "{\"request\":{\"resource\":{\"objectType\":\"<string>\",\"objectId\":\"<string>\"}," +
                "\"permission\":\"<string>\",\"subject\":{\"object\":{\"objectType\":\"<string>\",\"objectId\":" +
                "\"<string>\"},\"optionalRelation\":\"<string>\"},\"context\":{}},\"item\":{\"permissionship\":" +
                "\"PERMISSIONSHIP_UNSPECIFIED\",\"partialCaveatInfo\":{\"missingRequiredContext\":[\"<string>\"," +
                "\"<string>\"]}}}";

        BulkCheckPermissionResult result = new Gson().fromJson(jsonString, BulkCheckPermissionResult.class);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getResourceId());
        Assert.assertNotNull(result.getResourceType());
        Assert.assertNotNull(result.getPermission());
        Assert.assertNotNull(result.getSubjectId());
        Assert.assertNotNull(result.getContext());
    }

    @Test
    public void testGetResultWhenResultIsAvailable() throws Exception {

        BulkCheckPermissionResult result = new BulkCheckPermissionResult();
        Field resultField = result.getClass().getDeclaredField("result");
        resultField.setAccessible(true);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("permissionship", "PERMISSIONSHIP_UNSPECIFIED");
        resultField.set(result, resultMap);
        Field errorField = result.getClass().getDeclaredField("error");
        errorField.setAccessible(true);
        HashMap<String, Object> errorMap = new HashMap<>();
        errorMap.put("error", "error Occurred.");
        errorField.set(result, errorMap);

        Map<String, Object> resultValue = result.getResult();

        Assert.assertNotNull(resultValue);
        Assert.assertTrue(resultValue.containsKey("permissionship"));
        Assert.assertFalse(resultValue.containsKey("error"));
    }

    @Test
    public void testGetResultWhenResultIsNotAvailable() throws Exception {

        BulkCheckPermissionResult result = new BulkCheckPermissionResult();
        Field resultField = result.getClass().getDeclaredField("result");
        resultField.setAccessible(true);
        resultField.set(result, null);
        Field errorField = result.getClass().getDeclaredField("error");
        errorField.setAccessible(true);
        HashMap<String, Object> errorMap = new HashMap<>();
        errorMap.put("error", "error Occurred.");
        errorField.set(result, errorMap);

        Map<String, Object> resultValue = result.getResult();

        Assert.assertNotNull(resultValue);
        Assert.assertTrue(resultValue.containsKey("error"));
    }
}
