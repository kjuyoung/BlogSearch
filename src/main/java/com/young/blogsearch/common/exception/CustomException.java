package com.young.blogsearch.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;
    private HttpStatusCode httpStatus;
    private String message;

    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(HttpStatusCode httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
