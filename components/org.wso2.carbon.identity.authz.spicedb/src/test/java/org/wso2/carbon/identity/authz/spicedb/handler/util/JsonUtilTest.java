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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test class for {@link JsonUtil}.
 */
@Test
public class JsonUtilTest {

    @Test
    public void testParseToJsonString() {

        TestModel1 testModel1 = new TestModel1("test", new TestModel2("test", "test"), null);
        String expectedJson = "{\"firstAttribute\":\"test\",\"secondAttribute\":{\"firstAttribute\":\"test\"}}";
        String jsonString = JsonUtil.parseToJsonString(testModel1);

        Assert.assertNotNull(jsonString);
        Assert.assertEquals(jsonString, expectedJson);
    }

    @Test
    public void testJsonToResponseModel() {

        String jsonString = "{\"firstAttribute\":\"test\",\"secondAttribute\":{\"firstAttribute\":\"test\"}}";
        TestModel1 expectedModel = new TestModel1("test", new TestModel2("test", null), null);
        TestModel1 actualModel = JsonUtil.jsonToResponseModel(jsonString, TestModel1.class);

        Assert.assertNotNull(actualModel);
        Assert.assertEquals(actualModel.attribute1, expectedModel.attribute1);
        Assert.assertEquals(actualModel.attribute2.attribute1, expectedModel.attribute2.attribute1);
    }

    /*
     * Test classes for serialization and deserialization.
     * These classes are used to test the JSON conversion methods.
     */
    private static class TestModel1 {

        @SerializedName("firstAttribute")
        @Expose
        private String attribute1;
        @SerializedName("secondAttribute")
        @Expose
        private TestModel2 attribute2;
        @SerializedName("thirdAttribute")
        @Expose
        private String attribute3;

        public TestModel1(String attribute1, TestModel2 attribute2, String attribute3) {
            this.attribute1 = attribute1;
            this.attribute2 = attribute2;
            this.attribute3 = attribute3;
        }
    }

    private static class TestModel2 {

        @SerializedName("firstAttribute")
        @Expose
        private String attribute1;
        private String attribute2;

        public TestModel2(String attribute1, String attribute2) {
            this.attribute1 = attribute1;
            this.attribute2 = attribute2;
        }
    }
}
