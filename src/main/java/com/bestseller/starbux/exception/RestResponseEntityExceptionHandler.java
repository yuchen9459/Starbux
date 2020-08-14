package com.bestseller.starbux.exception;

import com.bestseller.starbux.exception.errors.BadRequestException;
import com.bestseller.starbux.exception.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.bestseller.starbux")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<RestApiError> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<RestApiError> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<RestApiError> handleAnyOtherExceptions(Exception ex, HttpServletRequest request) {
        LOGGER.error(String.format("Unknown error occurred in path <%s>", request.getRequestURI()), ex);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private static ResponseEntity<RestApiError> response(HttpStatus httpStatus, String message, HttpServletRequest request) {
        LOGGER.warn(String.format("Responding with status <%s>. %s", httpStatus, message));
        RestApiError restApiError = new RestApiError(message, httpStatus, request);
        return new ResponseEntity<>(restApiError, httpStatus);
    }
}
