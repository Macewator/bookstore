package com.bookstore.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ZipValidator implements ConstraintValidator<Zip,String> {

    private String zipPattern;

    @Override
    public void initialize(Zip constraintAnnotation) {
        this.zipPattern = constraintAnnotation.zipPattern();
    }

    @Override
    public boolean isValid(String pattern, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.matches(zipPattern,pattern);
    }
}
