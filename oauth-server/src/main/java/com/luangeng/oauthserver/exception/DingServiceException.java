package com.luangeng.oauthserver.exception;

import org.springframework.security.core.AuthenticationException;

public class DingServiceException extends AuthenticationException {
    public DingServiceException(String msg, Throwable t) {
        super(msg, t);
    }

    public DingServiceException(String msg) {
        super(msg);
    }
}
