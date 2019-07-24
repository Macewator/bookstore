package com.bookstore.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String > {

    private String passwordPattern;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.passwordPattern = constraintAnnotation.passwordPattern();
    }

    @Override
    public boolean isValid(String pattern, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.matches(passwordPattern,pattern);
    }
}
