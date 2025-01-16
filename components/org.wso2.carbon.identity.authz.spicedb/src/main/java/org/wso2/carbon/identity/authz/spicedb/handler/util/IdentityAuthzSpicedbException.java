package org.wso2.carbon.identity.authz.spicedb.handler.util;

import org.wso2.carbon.identity.base.IdentityException;

/**
 *
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
