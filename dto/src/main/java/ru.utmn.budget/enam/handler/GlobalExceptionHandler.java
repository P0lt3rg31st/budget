package ru.utmn.budget.enam.handler;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;

import java.net.BindException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.MDC;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @ExceptionHandler({
            BadRequestException.class,
            IllegalArgumentException.class,
            MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            BindException.class
    })
    public org.springframework.http.ResponseEntity<ApiError> handleBadRequest(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, resolveMessage(ex), request, response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Validation failed";
        } else {
            message = "Validation failed: " + message;
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message, request, response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleHandlerMethodValidation(
            HandlerMethodValidationException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String message = ex.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : error.toString())
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Validation failed";
        } else {
            message = "Validation failed: " + message;
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message, request, response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(this::formatConstraintViolation)
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Validation failed";
        } else {
            message = "Validation failed: " + message;
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message, request, response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, response);
    }

    @ExceptionHandler({
            NotFoundException.class,
            NoSuchElementException.class
    })
    public org.springframework.http.ResponseEntity<ApiError> handleNotFound(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, resolveMessage(ex), request, response);
    }

    @ExceptionHandler({
            ConflictException.class,
            DataIntegrityViolationException.class
    })
    public org.springframework.http.ResponseEntity<ApiError> handleConflict(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String message = ex instanceof DataIntegrityViolationException
                ? "Request conflicts with current resource state"
                : resolveMessage(ex);

        return buildResponse(HttpStatus.CONFLICT, message, request, response);
    }

    @ExceptionHandler(ErrorResponseException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleSpringErrorResponse(
            ErrorResponseException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpStatusCode statusCode = ex.getStatusCode();
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        String message = ex.getBody() != null && ex.getBody().getDetail() != null
                ? ex.getBody().getDetail()
                : status.getReasonPhrase();

        return buildResponse(status, message, request, response);
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ApiError> handleUnexpected(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected internal server error",
                request,
                response
        );
    }

    private org.springframework.http.ResponseEntity<ApiError> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String requestId = resolveRequestId(request);
        String traceId = resolveTraceId(requestId);

        response.setHeader(REQUEST_ID_HEADER, requestId);

        ApiError body = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                OffsetDateTime.now(ZoneOffset.UTC),
                traceId
        );

        return org.springframework.http.ResponseEntity
                .status(status)
                .header(REQUEST_ID_HEADER, requestId)
                .body(body);
    }

    private String resolveRequestId(HttpServletRequest request) {
        String headerValue = request.getHeader(REQUEST_ID_HEADER);
        if (headerValue != null && !headerValue.isBlank()) {
            return headerValue;
        }

        Object attributeValue = request.getAttribute(REQUEST_ID_HEADER);
        if (attributeValue instanceof String s && !s.isBlank()) {
            return s;
        }

        return "req_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String resolveTraceId(String fallback) {
        String traceId = MDC.get("traceId");
        if (traceId == null || traceId.isBlank()) {
            traceId = MDC.get("X-B3-TraceId");
        }
        return (traceId == null || traceId.isBlank()) ? fallback : traceId;
    }

    private String resolveMessage(Exception ex) {
        return (ex.getMessage() == null || ex.getMessage().isBlank())
                ? "Request processing failed"
                : ex.getMessage();
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private String formatConstraintViolation(ConstraintViolation<?> violation) {
        return violation.getPropertyPath() + ": " + violation.getMessage();
    }
}