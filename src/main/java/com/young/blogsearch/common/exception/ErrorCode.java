package com.young.blogsearch.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 BAD_REQUESTS
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    INVALID_METHOD_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    MISSING_REQUEST_ARGUMENT(HttpStatus.BAD_REQUEST, "Missing required argument"),
    METHOD_PARAMETER_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "Invalid parameter included"),

    // 404 NOT FOUND
    NO_SUCH_ELEMENT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),

    // 429 TOO MANY REQUESTS
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many requests"),

    // 500 SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    private ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
