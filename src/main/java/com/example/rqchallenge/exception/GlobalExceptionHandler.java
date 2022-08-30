package com.example.rqchallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody
    ExceptionResponse handleInvalidGeometryException(InvalidDataException exception) {
        return new ExceptionResponse(ErrorCodes.INVALID_DATA_FOUND.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler(EmployeeDataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ExceptionResponse handleInvalidInputException(EmployeeDataNotFoundException exception) {
        return new ExceptionResponse(ErrorCodes.NO_DATA_FOUND.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public @ResponseBody
    ExceptionResponse handleInvalidGeometryException(HttpClientErrorException exception) {
        return new ExceptionResponse(ErrorCodes.TOO_MANY_READ_REQUESTS.getErrorCode(), exception.getMessage());
    }
}
