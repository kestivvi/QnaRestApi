package com.github.kestivvi.qna_rest_api.validation.exceptions;

public class NotContainsException extends RuntimeException {

    public NotContainsException(String className, String property, String name) {
        super("Can't delete " + property + " " + name + " of " + className + ", because it is not contains");
    }

}
