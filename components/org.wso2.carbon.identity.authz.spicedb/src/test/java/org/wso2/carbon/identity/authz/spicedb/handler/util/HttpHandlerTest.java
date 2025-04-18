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
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbApiConstants;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link HttpHandler}.
 */
@Test
public class HttpHandlerTest {

    private static final String TEST_ENDPOINT = "/test";
    private static final String TEST_REQUEST_BODY = "{\"key\":\"value\"}";
    private static final String BASE_URL = "http://localhost:5000";
    private static final String TEST_URL = BASE_URL + TEST_ENDPOINT;
    private static final String RESPONSE_STRING = "response\nline1\nline2\n";

    private URI uri;

    @BeforeMethod
    public void setUp() throws URISyntaxException {

        uri = new URI(TEST_URL);
    }

    @Test
    public void testSendPostRequest() throws Exception {

        try (MockedStatic<HttpClientBuilder> httpClientBuilderMock = mockStatic(HttpClientBuilder.class);
             MockedStatic<HttpHandler> httpHandlerMock = mockStatic(HttpHandler.class)) {

            httpHandlerMock.when(() -> HttpHandler.createRequestUrl(TEST_ENDPOINT))
                    .thenReturn(uri);

            CloseableHttpClient mockClient = mock(CloseableHttpClient.class);
            HttpClientBuilder mockBuilder = mock(HttpClientBuilder.class);
            httpClientBuilderMock.when(HttpClientBuilder::create).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenReturn(mockClient);

            // Mock response
            CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
            when(mockClient.execute(any(HttpPost.class))).thenReturn(mockResponse);

            httpHandlerMock.when(() -> HttpHandler.sendPOSTRequest(anyString(), anyString()))
                    .thenCallRealMethod();
            // Act
            CloseableHttpResponse result = HttpHandler.sendPOSTRequest(TEST_ENDPOINT, TEST_REQUEST_BODY);

            Assert.assertNotNull(result);
            Assert.assertEquals(result, mockResponse);

            // Verify request construction
            ArgumentCaptor<HttpPost> postCaptor = ArgumentCaptor.forClass(HttpPost.class);
            verify(mockClient).execute(postCaptor.capture());

            HttpPost capturedPost = postCaptor.getValue();
            Assert.assertEquals(capturedPost.getURI(), uri);
            Assert.assertEquals(
                    capturedPost.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue(),
                    SpiceDbApiConstants.CONTENT_TYPE);
            Assert.assertEquals(
                    capturedPost.getFirstHeader(HttpHeaders.AUTHORIZATION).getValue(),
                    SpiceDbApiConstants.PRE_SHARED_KEY);
            Assert.assertEquals(
                    EntityUtils.toString(capturedPost.getEntity()),
                    TEST_REQUEST_BODY);
        }
    }

    @Test
    public void testCreateRequestUrl() throws Exception {

        try (MockedStatic<IdentityUtil> identityUtilMock = mockStatic(IdentityUtil.class)) {
            identityUtilMock.when(() -> IdentityUtil.getProperty(anyString()))
                    .thenReturn(BASE_URL);
            URI result = HttpHandler.createRequestUrl(TEST_ENDPOINT);
            Assert.assertNotNull(result);
            Assert.assertEquals(result.toString(), TEST_URL);
        }
    }

    @Test
    public void testParseResponseToString() throws Exception {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(RESPONSE_STRING.getBytes());
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
        HttpEntity mockEntity = mock(HttpEntity.class);
        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockEntity.getContent()).thenReturn(inputStream);
        String result = HttpHandler.parseResponseToString(mockResponse);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, RESPONSE_STRING);
    }
}
