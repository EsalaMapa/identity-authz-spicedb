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

package org.wso2.carbon.identity.authz.spicedb.handler.exception;

import org.wso2.carbon.identity.authorization.framework.exception.AccessEvaluationException;

/**
 * The {@code SpicedbEvaluationException} class is a custom exception class for the SpiceDB connector and extends
 * the {@link AccessEvaluationException} class.
 */
public class SpicedbEvaluationException extends AccessEvaluationException {

    private static final long serialVersionUID = 9162001723200200001L;
    private String errorCode = null;

    public SpicedbEvaluationException(String message) {
        super(message);
    }

    public SpicedbEvaluationException(String errorCode, String message) {

        super(message);
        this.errorCode = errorCode;
    }

    public SpicedbEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpicedbEvaluationException(String errorCode, String message, Throwable cause) {

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
