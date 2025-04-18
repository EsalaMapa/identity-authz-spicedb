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
import org.wso2.carbon.identity.authorization.framework.model.BulkAccessEvaluationResponse;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link BulkCheckPermissionResponse}.
 */
@Test
public class BulkCheckPermissionResponseTest {

    @Test
    public void testClassCreationWithJson() {

        String jsonString = "{\"checkedAt\":{\"token\":\"token1\"},\"pairs\":[{\"request\":{\"resource\":" +
                "{\"objectType\":\"<string>\",\"objectId\":\"<string>\"},\"permission\":\"<string>\",\"subject\":" +
                "{\"object\":{\"objectType\":\"<string>\",\"objectId\":\"<string>\"},\"optionalRelation\":" +
                "\"<string>\"},\"context\":{}},\"item\":{\"permissionship\":\"PERMISSIONSHIP_UNSPECIFIED\"," +
                "\"partialCaveatInfo\":{\"missingRequiredContext\":[\"<string>\",\"<string>\"]}}},{\"request\":" +
                "{\"resource\":{\"objectType\":\"<string>\",\"objectId\":\"<string>\"},\"permission\":\"<string>\"," +
                "\"subject\":{\"object\":{\"objectType\":\"<string>\",\"objectId\":\"<string>\"}," +
                "\"optionalRelation\":\"<string>\"},\"context\":{}},\"error\":{\"code\":\"<integer>\"," +
                "\"message\":\"<string>\",\"details\":[{\"@type\":\"<string>\"},{\"@type\":\"<string>\"," +
                "\"pariatur8d6\":{},\"in51\":{},\"in_ec\":{}}]}}]}";

        BulkCheckPermissionResponse response = new Gson().fromJson(jsonString, BulkCheckPermissionResponse.class);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getToken());
        Assert.assertNotNull(response.getResults());
        Assert.assertEquals(response.getResults().size(), 2);
        Assert.assertNotNull(response.getResults().get(0));
    }

    @Test
    public void testToBulkAccessEvaluationResponseWithSuccessfulResult() throws Exception {

        BulkCheckPermissionResponse response = new BulkCheckPermissionResponse();
        BulkCheckPermissionResult result = mock(BulkCheckPermissionResult.class);

        when(result.isResultAvailable()).thenReturn(true);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put(SpiceDbModelConstants.PERMISSION_RESULT, SpiceDbModelConstants.HAS_PERMISSION);
        resultMap.put(SpiceDbModelConstants.PARTIAL_CAVEAT_INFO, new HashMap<>());
        when(result.getResult()).thenReturn(resultMap);

        Field field = BulkCheckPermissionResponse.class.getDeclaredField("results");
        field.setAccessible(true);
        field.set(response, Collections.singletonList(result));

        BulkAccessEvaluationResponse bulkAccessEvaluationResponse = response.toBulkAccessEvalResponse();

        Assert.assertNotNull(bulkAccessEvaluationResponse.getResults().get(0));
        Assert.assertTrue(bulkAccessEvaluationResponse.getResults().get(0).getDecision());
        Assert.assertNotNull(bulkAccessEvaluationResponse.getResults().get(0).getContext());
    }

    @Test
    public void testToBulkAccessEvaluationResponseWithErrorResult() throws Exception {

        BulkCheckPermissionResponse response = new BulkCheckPermissionResponse();
        BulkCheckPermissionResult result = mock(BulkCheckPermissionResult.class);

        when(result.isResultAvailable()).thenReturn(false);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put(SpiceDbModelConstants.ERROR, "error");
        when(result.getResult()).thenReturn(resultMap);

        Field field = BulkCheckPermissionResponse.class.getDeclaredField("results");
        field.setAccessible(true);
        field.set(response, Collections.singletonList(result));

        BulkAccessEvaluationResponse bulkAccessEvaluationResponse = response.toBulkAccessEvalResponse();

        Assert.assertNotNull(bulkAccessEvaluationResponse.getResults().get(0));
        Assert.assertFalse(bulkAccessEvaluationResponse.getResults().get(0).getDecision());
        Assert.assertNotNull(bulkAccessEvaluationResponse.getResults().get(0).getContext());
    }
}
