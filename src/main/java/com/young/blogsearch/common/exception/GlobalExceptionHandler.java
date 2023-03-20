package com.young.blogsearch.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.UriComponentsBuilder;

import com.young.blogsearch.common.exception.ErrorResponse.ErrorResponseBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler  {

    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode, final Exception ex, final WebRequest request) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                            .body(makeErrorResponse(errorCode, ex, request));
    }

    private ErrorResponse makeErrorResponse(final ErrorCode errorCode, final Exception ex, final WebRequest request) {
        ErrorResponseBuilder error = ErrorResponse.getErrorResponseBuilder(((ServletWebRequest) request).getRequest(), errorCode);
        error.message(errorCode.getMessage());

        String referer = ((ServletWebRequest) request).getRequest()
                                                    .getHeader(HttpHeaders.REFERER);
        if (StringUtils.hasText(referer)) {
            UriComponentsBuilder refererBuilder = UriComponentsBuilder.fromHttpUrl(referer);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
            error.path(uriBuilder.path(refererBuilder.build()
                                                    .getPath())
                                .build()
                                .toString());
        }

        if (ex instanceof BindException || ex instanceof MethodArgumentNotValidException) {
            List<ErrorResponse.ValidationError> validationErrors = ((BindException) ex).getBindingResult()
                                                                                        .getFieldErrors()
                                                                                        .stream()
                                                                                        .map(v -> new ErrorResponse.ValidationError(v.getField(), v.getDefaultMessage()))
                                                                                        .collect(Collectors.toList());
            error.validationErrors(validationErrors);
        }
        return error.build();
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustomException(final CustomException ex, final WebRequest request) {
        log.error("handleCustomException {}", ex.getErrorCode().name());
        return handleExceptionInternal(ex.getErrorCode(), ex, request);
    }

    // 400 BAD_REQUEST
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex,
                                                                           final WebRequest request) {
        log.error("handleDataIntegrityViolationException {}", ex.getMessage());
        return handleExceptionInternal(ErrorCode.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameterException(final MissingServletRequestParameterException ex,
                                                                           final WebRequest request) {
        log.error("handleMissingServletRequestParameterException {}", ex.getMessage());
        return handleExceptionInternal(ErrorCode.MISSING_REQUEST_ARGUMENT, ex, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex,
                                                                                   final WebRequest request) {
        log.error("handleMethodArgumentTypeMismatchException {}", ex.getMessage());
        return handleExceptionInternal(ErrorCode.METHOD_PARAMETER_TYPE_MISMATCH, ex, request);
    }

    // 404 NOT_FOUND
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleItemNotFoundException(final RuntimeException ex, final WebRequest request) {
        String message = ex.getMessage();
        log.error("handleItemNotFoundException {}", message);
        return handleExceptionInternal(ErrorCode.NO_SUCH_ELEMENT_FOUND, ex, request);
    }

    // 429 TOO_MANY_REQUESTS
    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    protected ResponseEntity<Object> handleTooManyRequestsException(final RuntimeException ex, final WebRequest request) {
        String message = ex.getMessage();
        log.error("handleTooManyRequestsException {}", message);
        return handleExceptionInternal(ErrorCode.TOO_MANY_REQUESTS, ex, request);
    }

    // 500 INTERNAL_SERVER_ERROR
    @ExceptionHandler({ NullPointerException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        String message = ex.getMessage();
        log.error("handleInternal {}", message);
        return handleExceptionInternal(ErrorCode.INTERNAL_SERVER_ERROR, ex, request);
    }
}