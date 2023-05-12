package com.github.kestivvi.qna_rest_api.validation.exceptions;

public class DuplicateException extends RuntimeException {

    public DuplicateException(String className, String property, String name) {
        super("Can't create " + className + " with property " + property + ": " + name + ", already exists");
    }
}
