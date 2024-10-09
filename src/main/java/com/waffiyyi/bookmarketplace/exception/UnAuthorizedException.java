package com.waffiyyi.bookmarketplace.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UnAuthorizedException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public UnAuthorizedException(String message, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = message;

    }
}