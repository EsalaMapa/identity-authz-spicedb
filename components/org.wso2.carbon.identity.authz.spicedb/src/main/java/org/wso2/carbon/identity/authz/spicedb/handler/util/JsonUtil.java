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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The {@code JsonUtil} class is a utility class for JSON parsing. it holds the methods to convert Model classes to
 * JSON strings and vice versa.
 */
public class JsonUtil {

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    /**
     * Converts a request model to a JSON string. The model passed should be annotated with gson annotations to specify
     * which fields to include in the JSON string.
     *
     * @param requestModel The request model to be converted.
     * @param <T>         The type of the request model.
     * @return The JSON string representation of the request model.
     */
    public static <T> String parseToJsonString(T requestModel) {

       return GSON.toJson(requestModel, requestModel.getClass());
    }

    /**
     * Converts a JSON string to a response model type. The response model should be annotated with gson annotations to
     * specify which fields to convert from the JSON string.
     *
     * @param response The JSON string to be converted.
     * @param clazz    The class of the response model.
     * @param <T>      The type of the response model.
     * @return The response model representation of the JSON string.
     */
    public static <T> T jsonToResponseModel(String response, Class<T> clazz) {

        return GSON.fromJson(response, clazz);
    }

}
