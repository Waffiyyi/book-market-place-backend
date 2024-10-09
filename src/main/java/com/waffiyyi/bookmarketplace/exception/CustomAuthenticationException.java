package com.waffiyyi.bookmarketplace.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;
@Getter
@Setter
public class CustomAuthenticationException extends AuthenticationException {
    public CustomAuthenticationException(String msg) {
        super(msg);
    }

    public CustomAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}