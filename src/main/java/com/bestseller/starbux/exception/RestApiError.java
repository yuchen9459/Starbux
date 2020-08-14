package com.bestseller.starbux.exception;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

import static java.time.ZonedDateTime.now;

public class RestApiError {

    private HttpStatus httpStatus;
    private String message;
    private ZonedDateTime timestamp;
    private String path;

    public RestApiError(String message, HttpStatus httpStatus, HttpServletRequest request) {
        this.message = message;
        this.httpStatus = httpStatus;
        timestamp = now();
        path = request.getRequestURI();
    }

    public Integer getStatus() {
        if (httpStatus == null) {
            return null;
        }
        return httpStatus.value();
    }

    public String getError() {
        if (httpStatus == null) {
            return null;
        }
        return httpStatus.getReasonPhrase();
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
