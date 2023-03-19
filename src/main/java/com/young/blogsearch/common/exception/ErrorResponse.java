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

    // @Valid를 사용했을 때 에러가 발생한 경우 어느 필드에서 에러가 발생했는지 응답
    // @JsonInclude 는 Jackson 어노테이션으로 errors 가 null 이거나 empty 이면 전달하지 않기 위해 NON_EMPTY 적용
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
