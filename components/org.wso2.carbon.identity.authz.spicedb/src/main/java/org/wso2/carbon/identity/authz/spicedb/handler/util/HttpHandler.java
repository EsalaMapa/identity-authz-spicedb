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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbApiConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * The {@code HttpHandler} class is responsible for handling HTTP requests.
 *<P>
 *     This class provides methods to send GET and POST requests to the SpiceDB server and uses the Apache HttpClient.
 *</P>
 */
public class HttpHandler {

    /**
     * Sends a GET request to the specified URL.
     *
     * @param url The URL to which the GET request should be sent.
     * @return The HTTP response received from the server.
     * @throws IOException If an I/O error occurs.
     */
    public static HttpResponse sendGETRequest(String url) throws IOException {

        HttpResponse response;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
        }
        return response;
    }

    /**
     * Sends a POST request to the specified URL with the given JSON object.
     *
     * @param url The URL to which the POST request should be sent.
     * @param requestBody The JSON object to be sent with the POST request.
     * @return The HTTP response received from the server.
     * @throws IOException If an I/O error occurs.
     */
    public static CloseableHttpResponse sendPOSTRequest(String url, String requestBody) throws IOException {

        CloseableHttpResponse response;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", SpiceDbApiConstants.PRE_SHARED_KEY);
        httpPost.setEntity(new StringEntity(requestBody));
        response = httpClient.execute(httpPost);
        return response;
    }

    /**
     * Parses the content of the HTTP response to a string.
     *
     * @param response The HTTP response to be parsed.
     * @return The content of the HTTP response as a string.
     * @throws IOException If an I/O error occurs.
     */
    public static String parseResponseToString(HttpResponse response) throws IOException {

        HttpEntity entity = response.getEntity();
        InputStream stream = entity.getContent();
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine = bufferedReader.readLine();
            while (inputLine != null) {
                stringBuilder.append(inputLine).append("\n");
                inputLine = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        }
    }
}
