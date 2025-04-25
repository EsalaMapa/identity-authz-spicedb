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

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.wso2.carbon.identity.authorization.framework.model.AuthorizationAction;
import org.wso2.carbon.identity.authorization.framework.model.SearchActionsRequest;
import org.wso2.carbon.identity.authorization.framework.model.SearchActionsResponse;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbApiConstants;
import org.wso2.carbon.identity.authz.spicedb.handler.exception.SpicedbEvaluationException;
import org.wso2.carbon.identity.authz.spicedb.handler.model.Definition;
import org.wso2.carbon.identity.authz.spicedb.handler.model.OptionalSchemaFilter;
import org.wso2.carbon.identity.authz.spicedb.handler.model.ReadRelationshipsRequest;
import org.wso2.carbon.identity.authz.spicedb.handler.model.ReadRelationshipsResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.ReadRelationshipsResult;
import org.wso2.carbon.identity.authz.spicedb.handler.model.ReflectSchemaRequest;
import org.wso2.carbon.identity.authz.spicedb.handler.model.ReflectSchemaResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.Resource;
import org.wso2.carbon.identity.authz.spicedb.handler.model.SpiceDbErrorResponse;
import org.wso2.carbon.identity.authz.spicedb.handler.model.Subject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The {@code SearchActionsUtil} class provides utility methods for handling search actions requests since spiceDB does
 * not support search actions. This class uses Read Relationships endpoint and Schema Reflection endpoint to retrieve
 * the action details from SpiceDB.
 */
public class SearchActionsUtil {

    /**
     * This method retrieves the list of relations that the requested subject has with the requested resource using
     * the Read Relationships endpoint in SpiceDB.
     *
     * @param searchActionsRequest The request object containing the resource and subject details.
     * @return A list of relations.
     * @throws SpicedbEvaluationException If an error occurs while retrieving the relations.
     */
    public static ArrayList<String> getRelations(SearchActionsRequest searchActionsRequest)
            throws SpicedbEvaluationException {

        ReadRelationshipsRequest readRelationshipsRequest = createReadRelationshipsRequest(searchActionsRequest);
        try (CloseableHttpResponse response = HttpHandler.sendPOSTRequest(SpiceDbApiConstants.RELATIONSHIPS_READ,
                JsonUtil.parseToJsonString(readRelationshipsRequest))) {
            String responseString = HttpHandler.parseResponseToString(response);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return getListOfRelations(responseString);
            } else if (HttpStatus.SC_BAD_REQUEST <= statusCode &&
                    statusCode <= HttpStatus.SC_INSUFFICIENT_STORAGE) {
                SpiceDbErrorResponse error = JsonUtil.jsonToResponseModel(responseString, SpiceDbErrorResponse.class);
                throw new SpicedbEvaluationException(error.getCode(), error.getMessage());
            } else {
                throw new SpicedbEvaluationException("Reading relationships to search actions from spiceDB failed. " +
                        "Cannot identify error code.");
            }
        } catch (IOException e) {
            throw new SpicedbEvaluationException("Could not connect to SpiceDB to read relationships " +
                    "for action search.", e.getMessage());
        } catch (URISyntaxException ue) {
            throw new SpicedbEvaluationException("URI error occurred while creating the request URL. Could not " +
                    "connect to SpiceDB to read relationships for action search.", ue.getMessage());
        }

    }

    /**
     * This method retrieves the relations from the response object.
     *
     * @param responseString The response string from the Read Relationships endpoint.
     * @return A list of relations.
     */
    private static ArrayList<String> getListOfRelations(String responseString) {

        ReadRelationshipsResponse readRelationshipsResponse = new ReadRelationshipsResponse(responseString);
        ArrayList<String> relations = new ArrayList<>();
        for (ReadRelationshipsResult result : readRelationshipsResponse.getResults()) {
            if (result == null ||
                    result.getResultDetails().getRelation() == null) {
                continue;
            }
            relations.add(result.getResultDetails().getRelation());
        }
        return relations;
    }

    /**
     * This method retrieves the actions the subject can perform from the retrieved relations using the Reflect Schema
     * endpoint in SpiceDB.
     *
     * @param relations The list of relations.
     * @param resourceType The type of the resource.
     * @return {@link SearchActionsResponse} containing the list of actions.
     * @throws SpicedbEvaluationException If an error occurs while retrieving the actions.
     */
    public static SearchActionsResponse getActionsFromRelations(ArrayList<String> relations, String resourceType)
            throws SpicedbEvaluationException {

        if (relations.isEmpty()) {
            return new SearchActionsResponse(new ArrayList<>());
        }
        ReflectSchemaRequest reflectSchemaRequest =
                getReflectSchemaRequest(relations, resourceType);
        try (CloseableHttpResponse response = HttpHandler.sendPOSTRequest(SpiceDbApiConstants.REFLECT_SCHEMA,
                JsonUtil.parseToJsonString(reflectSchemaRequest))) {
            String responseString = HttpHandler.parseResponseToString(response);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                ReflectSchemaResponse reflectSchemaResponse = JsonUtil.jsonToResponseModel(
                        responseString, ReflectSchemaResponse.class);
                ArrayList<String> actions = new ArrayList<>();
                for (Definition definition : reflectSchemaResponse.getDefinitions()) {
                    actions.addAll(definition.getPermissionNames());
                }
                return new SearchActionsResponse(
                        actionsToAuthorizationActions(actions));
            } else if (HttpStatus.SC_BAD_REQUEST <= statusCode &&
                    statusCode <= HttpStatus.SC_INSUFFICIENT_STORAGE) {
                SpiceDbErrorResponse error = JsonUtil.jsonToResponseModel(responseString, SpiceDbErrorResponse.class);
                throw new SpicedbEvaluationException(error.getCode(), error.getMessage());
            } else {
                throw new SpicedbEvaluationException("Looking up subjects from spiceDB failed. " +
                        "Cannot identify error code.");
            }
        } catch (IOException e) {
            throw new SpicedbEvaluationException("Could not connect to SpiceDB to lookup subjects.",
                    e.getMessage());
        } catch (URISyntaxException ue) {
            throw new SpicedbEvaluationException("URI error occurred while creating the request URL. Could not " +
                    "connect to SpiceDB to look up subjects.", ue.getMessage());
        }
    }

    /**
     * This method creates a Read Relationships request object from the Search Actions request object.
     *
     * @param searchActionsRequest The Search Actions request object.
     * @return A {@link ReadRelationshipsRequest} object.
     */
    private static ReadRelationshipsRequest createReadRelationshipsRequest(SearchActionsRequest searchActionsRequest) {

        if (searchActionsRequest.getResource() == null ||
                searchActionsRequest.getSubject() == null) {

            throw new IllegalArgumentException("Invalid request. Resource and Subject must be provided " +
                    "to search actions.");
        }
        if (searchActionsRequest.getResource().getResourceId() == null ||
                searchActionsRequest.getSubject().getSubjectId() == null) {

            throw new IllegalArgumentException("Invalid request. Resource Id and Subject Id must be provid" +
                    "to search actions.");
        }
        Resource resource = new Resource(searchActionsRequest.getResource().getResourceType(),
                searchActionsRequest.getResource().getResourceId());
        Subject subject = new Subject(searchActionsRequest.getSubject().getSubjectType(),
                searchActionsRequest.getSubject().getSubjectId());
        return new ReadRelationshipsRequest(resource, subject);
    }

    /**
     * This method creates a Reflect Schema request object from the list of relations and resource type.
     *
     * @param relations The list of relations.
     * @param resourceType The type of the resource.
     * @return A {@link ReflectSchemaRequest} object.
     */
    private static ReflectSchemaRequest getReflectSchemaRequest(ArrayList<String> relations, String resourceType) {

        ArrayList<OptionalSchemaFilter> optionalSchemaFilters = new ArrayList<>();
        for (String relation : relations) {
            OptionalSchemaFilter optionalSchemaFilter = new OptionalSchemaFilter();
            optionalSchemaFilter.setOptionalRelationNameFilter(relation);
            optionalSchemaFilter.setOptionalDefinitionNameFilter(resourceType);
            optionalSchemaFilters.add(optionalSchemaFilter);
        }
        return new ReflectSchemaRequest(optionalSchemaFilters);
    }

    /**
     * This method converts a list of action strings to a list of {@link AuthorizationAction} objects to send back in
     * the response.
     *
     * @param actions The list of action strings.
     * @return A list of {@link AuthorizationAction} objects.
     */
    private static ArrayList<AuthorizationAction> actionsToAuthorizationActions(ArrayList<String> actions) {

        ArrayList<AuthorizationAction> authorizationActions = new ArrayList<>();
        if (actions == null || actions.isEmpty()) {
            return authorizationActions;
        }
        for (String action : actions) {
            AuthorizationAction authorizationAction = new AuthorizationAction(action);
            authorizationActions.add(authorizationAction);
        }
        return authorizationActions;
    }

}
