package com.github.kestivvi.qna_rest_api.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Service;
import com.github.kestivvi.qna_rest_api.validation.exceptions.ArgumentNotValidException;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public abstract class CustomValidator<T> {

    public final Validator validator;
    public final String className;

    public CustomValidator(Validator validator) {
        this.validator = validator;
        this.className = GenericTypeResolver.resolveTypeArgument(getClass(), CustomValidator.class).getSimpleName();
    }

    public void validateInput(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            Map<String, String> messages = new HashMap<>();
            for (ConstraintViolation<T> violation : violations) {
                messages.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new ArgumentNotValidException("Not valid data provided", messages);
        }
    }

}
