package com.github.kestivvi.qna_rest_api.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ValidationErrorBody extends ErrorBody{

    private Map<String, String> validationErrors;

    public ValidationErrorBody(String errorCode , String details) {
        super(errorCode, details);
        this.validationErrors = new HashMap<>();
    }
}
