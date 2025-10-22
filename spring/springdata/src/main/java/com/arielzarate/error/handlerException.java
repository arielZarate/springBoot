package com.arielzarate.error;

import com.arielzarate.error.model.ApplicationError;
import com.arielzarate.error.model.exception.ApplicationErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class handlerException {

    @ExceptionHandler(ApplicationErrorException.class)
    public ResponseEntity<ApplicationError> handleApplicationException(ApplicationErrorException ex) {
        ApplicationError error = ex.getError();
        return ResponseEntity.status(error.getStatus()).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationError> genericException(Exception ex) {
        ApplicationError error = ApplicationError.serverError(ex.getMessage());
        return ResponseEntity.status(error.getStatus()).body(error);
    }


}
