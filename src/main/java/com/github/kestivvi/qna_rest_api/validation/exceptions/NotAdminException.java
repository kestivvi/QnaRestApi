package com.github.kestivvi.qna_rest_api.validation.exceptions;

public class NotAdminException extends RuntimeException {

    public NotAdminException() {
        super("User has not admin privileges");
    }
}
