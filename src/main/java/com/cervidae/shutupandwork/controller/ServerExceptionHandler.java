package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.util.Response;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ServerExceptionHandler {

    /* AOP exception handlers */

    /**
     * All Exceptions
     * @param e Exception instance thrown
     * @return fail response
     */
    @ExceptionHandler(Exception.class)
    public Response<?> allExceptionHandler(Exception e) {
        return Response.fail(e.getMessage());
    }

    /**
     * IllegalArgumentException: thrown in case of illegal arguments provided by the request
     * @param e Exception instance thrown
     * @return fail response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Response<?> invalidParameterHandler(Exception e) {
        return Response.fail(e.getMessage());
    }

    /**
     * DataAccessException: thrown in case of database related errors
     * @param e Exception instance thrown
     * @return fail response
     */
    @ExceptionHandler(DataAccessException.class)
    public Response<?> dataAccessExceptionHandler(Exception e) {
        return Response.fail(e.getMessage());
    }
}
