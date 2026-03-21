package com.fake_store_login.error;

import com.fake_store_login.error.model.ApplicationError;
import com.fake_store_login.error.model.ClientError;
import com.fake_store_login.error.model.exception.ApplicationException;
import com.fake_store_login.error.model.exception.ClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class handlerException {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationError> handleApplicationException(ApplicationException ex) {
        ApplicationError error = ex.getError();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<ClientError> handleClientException(ClientException ex) {
        ClientError error = (ClientError) ex.getError();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationError> genericException(Exception ex) {
        ApplicationError error = ApplicationError.serverError(ex.getMessage());
        return ResponseEntity.status(error.getStatus()).body(error);
    }

}
