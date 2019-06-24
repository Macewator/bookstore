package com.bookstore.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TelephoneNumberValidator implements ConstraintValidator<TelephoneNumber, String> {

    private Integer digitsNumber;

    @Override
    public void initialize(TelephoneNumber constraintAnnotation) {
        this.digitsNumber = constraintAnnotation.digitsNumber();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value.length() == digitsNumber;
    }
}
