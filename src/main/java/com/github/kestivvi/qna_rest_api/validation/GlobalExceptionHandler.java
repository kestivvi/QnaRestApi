package com.github.kestivvi.qna_rest_api.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.github.kestivvi.qna_rest_api.validation.exceptions.*;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(new ErrorBody("401", "Not logged in"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorBody("403", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleArgumentNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorBody("404", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ValidationErrorBody> handleArgumentNotFoundException(ArgumentNotValidException ex) {
        ValidationErrorBody errorBody = new ValidationErrorBody("400", ex.getMessage());
        errorBody.setValidationErrors(ex.getViolations());
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleDuplicateException(DuplicateException ex) {
        return new ResponseEntity<>(new ErrorBody("400", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(NotContainsException ex) {
        return new ResponseEntity<>(new ErrorBody("404", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(NotAuthorizedException ex) {
        return new ResponseEntity<>(new ErrorBody("403", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleException(NotAdminException ex) {
        return new ResponseEntity<>(new ErrorBody("403", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

}
