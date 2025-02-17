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

import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.carbon.identity.oauth2.fga.models.ErrorResponse;
import org.wso2.carbon.identity.oauth2.fga.models.ListObjectsResponse;
import org.wso2.carbon.identity.oauth2.fga.models.ListObjectsResult;

import java.util.ArrayList;

/**
 *
 */
public class LookupObjectsResponse {

    private static final String RESULT = "result";
    private static final String ERROR = "error";

    private final ArrayList<JSONObject> results;
    private final ArrayList<JSONObject> errorResults;

    public LookupObjectsResponse(JSONArray objects) {

        this.results = new ArrayList<>();
        this.errorResults = new ArrayList<>();
        if (!objects.isEmpty()) {
            for (int i = 0; i < objects.length(); i++) {
                JSONObject object = objects.getJSONObject(i);
                if (object.has(RESULT)) {
                    this.results.add(object);
                } else if (object.has(ERROR)) {
                    this.errorResults.add(object);
                }
            }
        }
    }

    public ArrayList<JSONObject> getResults () {

        return results;
    }

    public ArrayList<JSONObject> getErrorResults () {

        return errorResults;
    }

    public ListObjectsResponse resourcesToListObjectsResponse() {

        ArrayList<ListObjectsResult> listObjectsResults = new ArrayList<>();
        for (JSONObject result : results) {
            LookupResourcesObject lookupResourcesObject = new LookupResourcesObject(result);
            ListObjectsResult listObjectsResult = new ListObjectsResult(lookupResourcesObject.getResourceId());
            listObjectsResult.setAdditionalContext(lookupResourcesObject.getPartialCaveatInfo());
            listObjectsResults.add(listObjectsResult);
        }
        ArrayList<ErrorResponse> errorResponses = addErrorResults();
        return new ListObjectsResponse(listObjectsResults, errorResponses);
    }

    public ListObjectsResponse subjectsToListObjectsResponse() {

        ArrayList<ListObjectsResult> listObjectsResults = new ArrayList<>();
        for (JSONObject result : results) {
            LookupSubjectsObject lookupSubjectsObject = new LookupSubjectsObject(result);
            ListObjectsResult listObjectsResult = new ListObjectsResult(lookupSubjectsObject.getSubjectId());
            listObjectsResult.setAdditionalContext(lookupSubjectsObject.getPartialCaveatInfo());
            listObjectsResults.add(listObjectsResult);
        }
        ArrayList<ErrorResponse> errorResponses = addErrorResults();
        return new ListObjectsResponse(listObjectsResults, errorResponses);
    }

    private ArrayList<ErrorResponse> addErrorResults () {

        ArrayList<ErrorResponse> errorResponses = new ArrayList<>();
        for (JSONObject errorResult : errorResults) {
            JSONObject error = errorResult.getJSONObject(ERROR);
            SpiceDbErrorResponse spiceDbErrorResponse = new SpiceDbErrorResponse(error);
            ErrorResponse errorResponse = new ErrorResponse(spiceDbErrorResponse.getMessage());
            errorResponse.setErrorCode(spiceDbErrorResponse.getCode());
            errorResponse.setErrorDetails(spiceDbErrorResponse.getDetails());
            errorResponses.add(errorResponse);
        }
        return errorResponses;
    }
}
