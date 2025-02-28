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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbConstants;
import org.wso2.carbon.identity.authz.spicedb.handler.model.LookupObjectsResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.LookupResourcesRequest;
import org.wso2.carbon.identity.authz.spicedb.handler.model.LookupSubjectsRequest;
import org.wso2.carbon.identity.authz.spicedb.handler.model.PermissionBulkCheckRequest;
import org.wso2.carbon.identity.authz.spicedb.handler.model.PermissionBulkCheckResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.PermissionCheckRequest;
import org.wso2.carbon.identity.authz.spicedb.handler.model.PermissionCheckResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.SpiceDbErrorResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.util.IdentityAuthzSpicedbException;
import org.wso2.carbon.identity.authz.spicedb.handler.util.SpiceDbHttpHandler;
import org.wso2.carbon.identity.oauth2.fga.models.AuthzBulkCheckRequest;
import org.wso2.carbon.identity.oauth2.fga.models.AuthzBulkCheckResponse;
import org.wso2.carbon.identity.oauth2.fga.models.AuthzCheckRequest;
import org.wso2.carbon.identity.oauth2.fga.models.AuthzCheckResponse;
import org.wso2.carbon.identity.oauth2.fga.models.ListObjectsRequest;
import org.wso2.carbon.identity.oauth2.fga.models.ListObjectsResponse;
import org.wso2.carbon.identity.oauth2.fga.services.FGAuthorizationInterface;

import java.io.IOException;

/**
 * The {@code SpiceDbPermissionRequestsHandler} handles  sending all evaluation related requests to SpiceDB
 * authorization engine.
 * <p>
 *     This class implements the {@link FGAuthorizationInterface} and provides the implementation for the
 *     {@code checkAuthorization}, {@code bulkCheckAuthorization}, {@code lookUpResources} and {@code lookUpSubjects}
 *     methods. This class uses the {@link SpiceDbHttpHandler} to send HTTP requests to the SpiceDB authorization
 *     engine and parse the responses to generic response models.
 * </p>
 */
public class SpiceDbPermissionRequestsHandler implements FGAuthorizationInterface {

    private static final Log LOG = LogFactory.getLog(SpiceDbPermissionRequestsHandler.class);
    private static final SpiceDbHttpHandler SPICEDB_HTTP_HANDLER = new SpiceDbHttpHandler();


    /**
     * This method sends a permission check request to SpiceDB and returns the response as an
     * {@link AuthzCheckResponse}.
     *
     * @param authzCheckRequest The request object containing the permission check details.
     * @return The response object containing the authorization check result.
     * @throws IdentityAuthzSpicedbException If an error occurs while sending the request or parsing the response.
     */
    @Override
    public AuthzCheckResponse checkAuthorization(AuthzCheckRequest authzCheckRequest)
            throws IdentityAuthzSpicedbException {

        JSONObject object;
        PermissionCheckRequest permissionCheckRequest = new PermissionCheckRequest(authzCheckRequest);
        try (CloseableHttpResponse response = SPICEDB_HTTP_HANDLER.sendPOSTRequest(SpiceDbConstants.BASE_URL +
                SpiceDbConstants.PERMISSION_CHECK, permissionCheckRequest.parseToJSON())) {
            object = new JSONObject(SPICEDB_HTTP_HANDLER.parseToString(response));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == SpiceDbConstants.STATUS_OK) {
                PermissionCheckResponse permissionCheckResponse = new PermissionCheckResponse(object);
                return new AuthzCheckResponse(permissionCheckResponse.isAuthorized());
            } else if (SpiceDbConstants.BAD_REQUEST <= statusCode &&
                    statusCode <= SpiceDbConstants.NETWORK_AUTHENTICATION_REQUIRED) {
                SpiceDbErrorResponse error = new SpiceDbErrorResponse(object);
                throw new IdentityAuthzSpicedbException(error.getCode(), error.getMessage());
            } else {
                throw new IdentityAuthzSpicedbException("Authorization check from spiceDB failed. " +
                        "Cannot identify error code.");
            }
        } catch (IOException e) {
            throw new IdentityAuthzSpicedbException("Could not connect to SpiceDB to check authorization.",
                    e.getMessage());
        } catch (JSONException e) {
            throw new IdentityAuthzSpicedbException("Could not parse the response from SpiceDB.", e.getMessage());
        } catch (Exception e) {
            throw new IdentityAuthzSpicedbException("Authorization check from spiceDB failed.", e.getMessage());
        }
    }

    /**
     * This method sends a bulk permission check request to SpiceDB and returns the response as an
     * {@link AuthzBulkCheckResponse}.
     *
     * @param authzBulkCheckRequest The request object containing the bulk permission check details.
     * @return The response object containing the authorization bulk check result.
     * @throws IdentityAuthzSpicedbException If an error occurs while sending the request or parsing the response.
     */
    @Override
    public AuthzBulkCheckResponse bulkCheckAuthorization
            (AuthzBulkCheckRequest authzBulkCheckRequest) throws IdentityAuthzSpicedbException {

        JSONObject object;
        PermissionBulkCheckRequest permissionBulkCheckRequest =
                new PermissionBulkCheckRequest(authzBulkCheckRequest.getItems());
        try (CloseableHttpResponse response = SPICEDB_HTTP_HANDLER.sendPOSTRequest(SpiceDbConstants.BASE_URL +
                SpiceDbConstants.PERMISSIONS_BULKCHECK, permissionBulkCheckRequest.parseToJSON())) {
            object = new JSONObject(SPICEDB_HTTP_HANDLER.parseToString(response));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == SpiceDbConstants.STATUS_OK) {
                PermissionBulkCheckResponse permissionBulkCheckResponse = new PermissionBulkCheckResponse(object);
                return permissionBulkCheckResponse.toAuthzBulkCheckResponse();
            } else if (SpiceDbConstants.BAD_REQUEST <= statusCode &&
                    statusCode <= SpiceDbConstants.NETWORK_AUTHENTICATION_REQUIRED) {
                SpiceDbErrorResponse error = new SpiceDbErrorResponse(object);
                throw new IdentityAuthzSpicedbException(error.getCode(), error.getMessage());
            } else {
                throw new IdentityAuthzSpicedbException("Authorization bulk check from spiceDB failed. " +
                        "Cannot identify error code.");
            }
        } catch (IOException e) {
            throw new IdentityAuthzSpicedbException("Could not connect to SpiceDB to bulk check authorization.",
                    e.getMessage());
        } catch (JSONException e) {
            throw new IdentityAuthzSpicedbException("Could not parse the response from SpiceDB.", e.getMessage());
        } catch (Exception e) {
            throw new IdentityAuthzSpicedbException("Authorization bulk check from spiceDB failed.", e.getMessage());
        }
    }

    /**
     * This method sends a request to look up resources in SpiceDB and returns the response as a
     * {@link ListObjectsResponse}.
     *
     * @param listObjectsRequest The request object containing the resource details to look up.
     * @return The response object containing the list of resources.
     * @throws IdentityAuthzSpicedbException If an error occurs while sending the request or parsing the response.
     */
    @Override
    public ListObjectsResponse lookUpResources(ListObjectsRequest listObjectsRequest)
            throws IdentityAuthzSpicedbException {

        JSONArray objects = new JSONArray();
        String[] responseArray;
        LookupResourcesRequest lookupResourcesRequest = new LookupResourcesRequest(listObjectsRequest);
        try (CloseableHttpResponse response = SPICEDB_HTTP_HANDLER.sendPOSTRequest(SpiceDbConstants.BASE_URL +
                SpiceDbConstants.LOOKUP_RESOURCES, lookupResourcesRequest.parseToJSON())) {
            responseArray = SPICEDB_HTTP_HANDLER.parseToString(response).split("(?<=})\\s*(?=\\{)");
            if (responseArray[0].isEmpty()) {
                LOG.info("No resources found in the response from SpiceDB. Check the requested permissions again");
            }
            for (String responseString : responseArray) {
                objects.put(new JSONObject(responseString));
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == SpiceDbConstants.STATUS_OK) {
                LookupObjectsResponse lookupObjectsResponse = new LookupObjectsResponse(objects);
                return lookupObjectsResponse.resourcesToListObjectsResponse();
            } else if (SpiceDbConstants.BAD_REQUEST <= statusCode &&
                    statusCode <= SpiceDbConstants.NETWORK_AUTHENTICATION_REQUIRED) {
                SpiceDbErrorResponse error = new SpiceDbErrorResponse(objects.getJSONObject(0));
                throw new IdentityAuthzSpicedbException(error.getCode(), error.getMessage());
            } else {
                throw new IdentityAuthzSpicedbException("Looking up resources from spiceDB failed. " +
                        "Cannot identify error code.");
            }
        } catch (IOException e) {
            throw new IdentityAuthzSpicedbException("Could not connect to SpiceDB to look up resources authorization.",
                    e.getMessage());
        } catch (JSONException e) {
            throw new IdentityAuthzSpicedbException("Could not parse the response from SpiceDB.", e.getMessage());
        } catch (IdentityAuthzSpicedbException e) {
            throw e;
        } catch (Exception e) {
            throw new IdentityAuthzSpicedbException("Looking up resources from spiceDB failed.", e.getMessage());
        }
    }

    /**
     * This method sends a request to look up subjects in SpiceDB and returns the response as a
     * {@link ListObjectsResponse}.
     *
     * @param listObjectsRequest The request object containing the subject details to look up.
     * @return The response object containing the list of subjects.
     * @throws IdentityAuthzSpicedbException If an error occurs while sending the request or parsing the response.
     */
    @Override
    public ListObjectsResponse lookUpSubjects(ListObjectsRequest listObjectsRequest)
            throws IdentityAuthzSpicedbException {

        JSONArray objects = new JSONArray();
        String[] responseArray;
        LookupSubjectsRequest lookupSubjectsRequest = new LookupSubjectsRequest(listObjectsRequest);
        try (CloseableHttpResponse response = SPICEDB_HTTP_HANDLER.sendPOSTRequest(SpiceDbConstants.BASE_URL +
                SpiceDbConstants.LOOKUP_SUBJECTS, lookupSubjectsRequest.parseToJSON())) {
            responseArray = SPICEDB_HTTP_HANDLER.parseToString(response).split("(?<=})\\s*(?=\\{)");
            if (responseArray[0].isEmpty()) {
                LOG.info("No subjects found in the response from SpiceDB. Check the requested permissions again");
            }
            for (String responseString : responseArray) {
                objects.put(new JSONObject(responseString));
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == SpiceDbConstants.STATUS_OK) {
                LookupObjectsResponse lookupObjectsResponse = new LookupObjectsResponse(objects);
                return lookupObjectsResponse.subjectsToListObjectsResponse();
            } else if (SpiceDbConstants.BAD_REQUEST <= statusCode &&
                    statusCode <= SpiceDbConstants.NETWORK_AUTHENTICATION_REQUIRED) {
                SpiceDbErrorResponse error = new SpiceDbErrorResponse(objects.getJSONObject(0));
                throw new IdentityAuthzSpicedbException(error.getCode(), error.getMessage());
            } else {
                throw new IdentityAuthzSpicedbException("Looking up subjects from spiceDB failed. " +
                        "Cannot identify error code.");
            }
        } catch (IOException e) {
            throw new IdentityAuthzSpicedbException("Could not connect to SpiceDB to look up subjects authorization.",
                    e.getMessage());
        } catch (JSONException e) {
            throw new IdentityAuthzSpicedbException("Could not parse the response from SpiceDB.", e.getMessage());
        } catch (Exception e) {
            throw new IdentityAuthzSpicedbException("Looking up subjects from spiceDB failed.", e.getMessage());
        }
    }
}
