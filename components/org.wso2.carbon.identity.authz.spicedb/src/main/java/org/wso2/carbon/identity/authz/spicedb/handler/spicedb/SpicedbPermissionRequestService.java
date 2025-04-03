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

package org.wso2.carbon.identity.authz.spicedb.handler.spicedb;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.wso2.carbon.identity.authorization.framework.model.AccessEvaluationRequest;
import org.wso2.carbon.identity.authorization.framework.model.AccessEvaluationResponse;
import org.wso2.carbon.identity.authorization.framework.model.BulkAccessEvaluationRequest;
import org.wso2.carbon.identity.authorization.framework.model.BulkAccessEvaluationResponse;
import org.wso2.carbon.identity.authorization.framework.service.AccessEvaluationService;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbApiConstants;
import org.wso2.carbon.identity.authz.spicedb.handler.exception.SpicedbEvaluationException;
import org.wso2.carbon.identity.authz.spicedb.handler.model.BulkCheckPermissionRequest;
import org.wso2.carbon.identity.authz.spicedb.handler.model.BulkCheckPermissionResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.CheckPermissionRequest;
import org.wso2.carbon.identity.authz.spicedb.handler.model.CheckPermissionResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.SpiceDbErrorResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.util.HttpHandler;
import org.wso2.carbon.identity.authz.spicedb.handler.util.JsonUtil;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The {@code SpicedbPermissionRequestService} handles sending all evaluation related requests to SpiceDB
 * authorization engine.
 * <p>
 *     This class implements the {@link AccessEvaluationService} and provides the implementation for access evaluation
 *     using SpiceDB. This class uses the {@link HttpHandler} to send HTTP requests to the SpiceDB authorization
 *     engine and parse the responses to generic response models.
 * </p>
 */
public class SpicedbPermissionRequestService implements AccessEvaluationService {

    /**
     * This method returns the name of the authorization engine.
     *
     * @return The name of the authorization engine.
     */
    @Override
    public String getEngine() {

        return SpiceDbApiConstants.AUTHORIZATION_ENGINE_NAME;
    }

    /**
     * This method sends a permission check request to SpiceDB and returns the response as an
     * {@link AccessEvaluationResponse}.
     *
     * @param accessEvaluationRequest The request object containing the permission check details.
     * @return The response object containing the authorization check result.
     * @throws SpicedbEvaluationException If an error occurs while sending the request or parsing the response.
     */
    @Override
    public AccessEvaluationResponse evaluate(AccessEvaluationRequest accessEvaluationRequest)
            throws SpicedbEvaluationException {

        if (accessEvaluationRequest == null) {
            throw new SpicedbEvaluationException("Invalid request. Access evaluation request cannot be null.");
        }
        CheckPermissionRequest checkPermissionRequest = new CheckPermissionRequest(accessEvaluationRequest);
        try (CloseableHttpResponse response = HttpHandler.sendPOSTRequest(
                SpiceDbApiConstants.PERMISSION_CHECK,
                JsonUtil.parseToJsonString(checkPermissionRequest))) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseString = HttpHandler.parseResponseToString(response);
            if (statusCode == HttpStatus.SC_OK) {
                CheckPermissionResponse checkPermissionResponse = JsonUtil.jsonToResponseModel(
                        responseString, CheckPermissionResponse.class);
                AccessEvaluationResponse accessEvaluationResponse = new AccessEvaluationResponse(
                        checkPermissionResponse.isAuthorized());
                accessEvaluationResponse.setContext(checkPermissionResponse.getPartialCaveatInfo());
                return accessEvaluationResponse;
            } else if (HttpStatus.SC_BAD_REQUEST <= statusCode &&
                    statusCode <= HttpStatus.SC_INSUFFICIENT_STORAGE) {
                SpiceDbErrorResponse error = JsonUtil.jsonToResponseModel(
                        responseString, SpiceDbErrorResponse.class);
                throw new SpicedbEvaluationException(error.getCode(), error.getMessage());
            } else {
                throw new SpicedbEvaluationException("Authorization check from spiceDB failed. " +
                        "Cannot identify error code.");
            }
        } catch (IOException e) {
            throw new SpicedbEvaluationException("Could not connect to SpiceDB to check authorization.",
                    e.getMessage());
        } catch (URISyntaxException ue) {
            throw new SpicedbEvaluationException("URI error occurred while creating the request URL. Could not " +
                    "connect to SpiceDB for check authorization", ue.getMessage());
        }
    }

    /**
     * This method sends a bulk permission check request to SpiceDB and returns the response as an
     * {@link BulkAccessEvaluationResponse}.
     *
     * @param bulkAccessEvaluationRequest The request object containing the bulk permission check details.
     * @return The response object containing the authorization bulk check result.
     * @throws SpicedbEvaluationException If an error occurs while sending the request or parsing the response.
     */
    @Override
    public BulkAccessEvaluationResponse bulkEvaluate
    (BulkAccessEvaluationRequest bulkAccessEvaluationRequest) throws SpicedbEvaluationException {

        if (bulkAccessEvaluationRequest == null) {
            throw new SpicedbEvaluationException("Invalid request. Bulk access evaluation request cannot be null.");
        }
        BulkCheckPermissionRequest bulkCheckPermissionRequest =
                new BulkCheckPermissionRequest(bulkAccessEvaluationRequest.getRequestItems());
        try (CloseableHttpResponse response = HttpHandler.sendPOSTRequest(
                SpiceDbApiConstants.PERMISSIONS_BULKCHECK,
                JsonUtil.parseToJsonString(bulkCheckPermissionRequest))) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseString = HttpHandler.parseResponseToString(response);
            if (statusCode == HttpStatus.SC_OK) {
                BulkCheckPermissionResponse bulkCheckPermissionResponse = JsonUtil.jsonToResponseModel(
                        responseString, BulkCheckPermissionResponse.class);
                return bulkCheckPermissionResponse.toBulkAccessEvalResponse();
            } else if (HttpStatus.SC_BAD_REQUEST <= statusCode &&
                    statusCode <= HttpStatus.SC_INSUFFICIENT_STORAGE) {
                SpiceDbErrorResponse error = JsonUtil.jsonToResponseModel(
                        responseString, SpiceDbErrorResponse.class);
                throw new SpicedbEvaluationException(error.getCode(), error.getMessage());
            } else {
                throw new SpicedbEvaluationException("Authorization bulk check from spiceDB failed. " +
                        "Cannot identify error code.");
            }
        } catch (IOException e) {
            throw new SpicedbEvaluationException("Could not connect to SpiceDB to bulk check authorization.",
                    e.getMessage());
        } catch (URISyntaxException ue) {
            throw new SpicedbEvaluationException("URI error occurred while creating the request URL. Could not " +
                    "connect to SpiceDB for bulk check authorization", ue.getMessage());
        }
    }
}
