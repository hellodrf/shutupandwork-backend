package com.cervidae.shutupandwork.controller;

import com.cervidae.shutupandwork.util.Response;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class DBExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public Response<?> dataAccessExceptionHandle() {
        return Response.fail("database error");
    }
}
