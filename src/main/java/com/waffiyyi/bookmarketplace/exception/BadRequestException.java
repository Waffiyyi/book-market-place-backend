package com.waffiyyi.bookmarketplace.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequestException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;
    public BadRequestException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;

    }
}
