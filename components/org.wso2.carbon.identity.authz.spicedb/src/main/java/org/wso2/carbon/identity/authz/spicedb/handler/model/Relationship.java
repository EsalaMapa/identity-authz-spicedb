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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.wso2.carbon.identity.authz.spicedb.constants.SpiceDbModelConstants;

import java.util.Map;

/**
 * The {@code Relationship} class represents the relationship object in a read relationships responses and write
 * relationships requests sent to SpiceDB.
 */
@SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR",
        justification = "Field is populated via Gson deserialization")
public class Relationship {

    @SerializedName(SpiceDbModelConstants.RESOURCE)
    @Expose
    Resource resource;
    @SerializedName(SpiceDbModelConstants.RELATION)
    @Expose
    String relation;
    @SerializedName(SpiceDbModelConstants.SUBJECT)
    @Expose
    Subject subject;
    @SerializedName(SpiceDbModelConstants.OPTIONAL_CAVEAT)
    @Expose
    Map<String, Object> optionalCaveat;

    public void setResourceItem(Resource resource) {

        this.resource = resource;
    }

    public void setRelation(String relation) {

        this.relation = relation;
    }

    public void setSubjectItem(Subject subject) {

        this.subject = subject;
    }

    public void setOptionalCaveat(Map<String, Object> optionalCaveat) {

        this.optionalCaveat = optionalCaveat;
    }

    public Resource getResourceItem() {

        return resource;
    }
}
