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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

import java.util.Locale;

/**
 * The {@code SubjectDetails} class represents an object inside a {@link Subject} that used in requests and
 * responses.
 */
public class SubjectDetails {

    @SerializedName(SpiceDbModelConstants.OBJECT_TYPE)
    @Expose
    private String subjectType;
    @SerializedName(SpiceDbModelConstants.OBJECT_ID)
    @Expose
    private String subjectId;

    public SubjectDetails(String subjectType, String subjectId) {

        // Convert subject type to lower case since SpiceDB expects it to be in lower case.
        this.subjectType = subjectType.toLowerCase(Locale.ROOT);
        this.subjectId = subjectId;
    }

    public String getSubjectType() {

        return subjectType;
    }

    public String getSubjectId() {

        return subjectId;
    }
}
