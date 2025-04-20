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

import org.wso2.carbon.identity.authz.spicedb.handler.util.JsonUtil;

import java.util.ArrayList;

/**
 * The {@code ReadRelationshipsResponse} class represents the response of a read relationships request sent to SpiceDB.
 * The read results are returned as a stream of Json String objects. This class splits the stream and creates a list of
 * {@link ReadRelationshipsResultBody} objects.
 */
public class ReadRelationshipsResponse {

    private ArrayList<ReadRelationshipsResult> results;

    public ReadRelationshipsResponse(String response) {

        this.results = new ArrayList<>();
        String[] resultsArray = response.split("(?<=})\\s*(?=\\{)");
        for (String result : resultsArray) {
            this.results.add(JsonUtil.jsonToResponseModel(result, ReadRelationshipsResult.class));
        }
    }

    public ArrayList<ReadRelationshipsResult> getResults() {

        return this.results;
    }

//TODO: (Below method is to be used with the read data implementation.)

//    /**
//     * Converts the response to a {@link ReadAuthzDataResponse} object from a read relationships response.
//     *
//     * @return The {@link ReadAuthzDataResponse} object.
//     */
//    public ReadAuthzDataResponse toAuthzDataReadResponse() {
//        ArrayList<ReadAuthzDataResult> authzDataReadResultObjects =
//                new ArrayList<>();
//        for (ReadRelationshipsResultBody readRelationshipsResult : results) {
//            AuthorizationResource resourceObject = new AuthorizationResource(
//                    readRelationshipsResult.getResourceType(),
//                    readRelationshipsResult.getResourceId());
//            AuthorizationAction actionObject = new AuthorizationAction(readRelationshipsResult.getRelation());
//            AuthorizationSubject subjectObject = new AuthorizationSubject(
//                    readRelationshipsResult.getSubjectType(),
//                    readRelationshipsResult.getSubjectId());
//            ReadAuthzDataResult authzDataResultObject = new ReadAuthzDataResult(
//                    resourceObject, actionObject, subjectObject);
//            authzDataReadResultObjects.add(authzDataResultObject);
//        }
//        return new ReadAuthzDataResponse(authzDataReadResultObjects);
//    }
}
