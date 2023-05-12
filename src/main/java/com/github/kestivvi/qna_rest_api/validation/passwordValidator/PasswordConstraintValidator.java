package com.github.kestivvi.qna_rest_api.validation.passwordValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        } else if (password.isEmpty() || password.isBlank()) {
            return false;
        } else if (password.length() < 8) {
            return false;
        } else if (password.length() > 30) {
            return false;
        } else if (password.contains(" ")) {
            return false;
        } else if (password.contains("\t")) {
            return false;
        } else if (password.contains("\r")) {
            return false;
        } else {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();
        }

    }
}

