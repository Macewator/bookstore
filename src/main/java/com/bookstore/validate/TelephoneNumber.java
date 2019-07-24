package com.bookstore.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = TelephoneNumberValidator.class)
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface TelephoneNumber {
    String message() default "numer telefonu musi zawierać 11 cyfr";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int digitsNumber() default 11;
}
