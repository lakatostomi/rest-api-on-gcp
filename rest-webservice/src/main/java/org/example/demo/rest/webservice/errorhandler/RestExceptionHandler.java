package org.example.demo.rest.webservice.errorhandler;

import org.example.demo.rest.webservice.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    protected ProblemDetail handleClientsBadRequests(Exception ex) {
        ProblemDetail problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        log.warn(problemDetail.toString());
        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        ProblemDetail problemDetail = createProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        log.warn(problemDetail.toString());
        return problemDetail;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ProblemDetail handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ProblemDetail problemDetail = createProblemDetail(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage());
        log.warn(problemDetail.toString());
        return problemDetail;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ProblemDetail handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        ProblemDetail problemDetail = createProblemDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getMessage());
        log.warn(problemDetail.toString());
        return problemDetail;
    }

    private ProblemDetail createProblemDetail(int statusCode, String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(statusCode), message);
        problemDetail.setProperty("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return problemDetail;

    }

}
