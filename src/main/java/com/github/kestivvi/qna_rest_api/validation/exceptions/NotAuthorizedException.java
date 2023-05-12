package com.github.kestivvi.qna_rest_api.validation.exceptions;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String className, String id, String userId) {
        super("User with " + userId + " is not authorized to access " + className + " with id " + id);
    }
}
