package com.young.blogsearch.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final String timestamp;
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String path;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> validationErrors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {

        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                                .field(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                                .build();
        }
    }

    public static ErrorResponseBuilder getErrorResponseBuilder(HttpServletRequest request, ErrorCode errorCode) {
        ErrorResponseBuilder errorResponseDtoBuilder = ErrorResponse.builder();

        ZonedDateTime current = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        String timestamp = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZZZ"));

        errorResponseDtoBuilder
                .timestamp(timestamp)
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .path(request.getRequestURI());
        return errorResponseDtoBuilder;
    }

    public static ErrorResponse getErrorResponse(HttpServletRequest request, ErrorCode errorCode) {
        return getErrorResponseBuilder(request, errorCode).build();
    }
}
