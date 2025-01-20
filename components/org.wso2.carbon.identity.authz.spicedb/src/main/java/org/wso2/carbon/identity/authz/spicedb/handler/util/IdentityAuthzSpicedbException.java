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

import org.wso2.carbon.identity.base.IdentityException;

/**
 * This class is used to handle exceptions thrown by the SpiceDB authorization handler.
 */
public class IdentityAuthzSpicedbException extends IdentityException {

    private String errorCode = null;

    public IdentityAuthzSpicedbException (String message) {
        super(message);
    }

    public IdentityAuthzSpicedbException (String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IdentityAuthzSpicedbException (String message, Throwable cause) {
        super(message, cause);
    }

    public IdentityAuthzSpicedbException (String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
