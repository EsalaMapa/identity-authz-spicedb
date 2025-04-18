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

/**
 * Unit test class for {@link CheckPermissionResponse}.
 */
public class CheckPermissionResponseTest {

    @Test
    public void testClassCreationWithJson() {

        String jsonString = "{\"checkedAt\":{\"token\":\"token2\"},\"permissionship\":" +
                "\"PERMISSIONSHIP_UNSPECIFIED\",\"partialCaveatInfo\":{\"missingRequiredContext\":" +
                "[\"<string>\",\"<string>\"]},\"debugTrace\":{\"check\":{\"resource\":{\"objectType\":\"<string>\"," +
                "\"objectId\":\"<string>\"},\"permission\":\"<string>\",\"permissionType\":" +
                "\"PERMISSION_TYPE_UNSPECIFIED\",\"subject\":{\"object\":{\"objectType\":\"<string>\",\"objectId\":" +
                "\"<string>\"},\"optionalRelation\":\"<string>\"},\"result\":\"PERMISSIONSHIP_UNSPECIFIED\"," +
                "\"caveatEvaluationInfo\":{\"expression\":\"<string>\",\"result\":\"RESULT_UNSPECIFIED\",\"context\":" +
                "{},\"partialCaveatInfo\":{\"missingRequiredContext\":[\"<string>\",\"<string>\"]},\"caveatName\":" +
                "\"<string>\"},\"duration\":\"<string>\",\"wasCachedResult\":\"<boolean>\",\"subProblems\":{" +
                "\"traces\":[{\"value\":\"<Circular reference to #/components/schemas/v1CheckDebugTrace detected>\"}," +
                "{\"value\":\"<Circular reference to #/components/schemas/v1CheckDebugTrace detected>\"}]}}," +
                "\"schemaUsed\":\"<string>\"}}";

        CheckPermissionResponse checkPermissionResponse = new Gson().fromJson(jsonString,
                CheckPermissionResponse.class);

        Assert.assertNotNull(checkPermissionResponse);
        Assert.assertNotNull(checkPermissionResponse.getToken());
        Assert.assertEquals(checkPermissionResponse.getToken(), "token2");
        Assert.assertNotNull(checkPermissionResponse.getPermissionship());
        Assert.assertEquals(checkPermissionResponse.getPermissionship(), "PERMISSIONSHIP_UNSPECIFIED");
        Assert.assertNotNull(checkPermissionResponse.getPartialCaveatInfo());
        Assert.assertNotNull(checkPermissionResponse.getDebugTrace());
    }
}
