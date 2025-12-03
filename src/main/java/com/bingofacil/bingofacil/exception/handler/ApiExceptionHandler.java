package com.bingofacil.bingofacil.exception.handler;

import com.bingofacil.bingofacil.dtos.ResponseError;
import com.bingofacil.bingofacil.exception.custom.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseError> handleTelTelephoneRegistered(DataIntegrityViolationException ex){
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.CONFLICT;
        ResponseError erro = new ResponseError(message, status.value());

        return new ResponseEntity<>(erro, status);
    }

}
