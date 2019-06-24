package com.bookstore.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ZipValidator.class)
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Zip {
    String message() default "Postal code should be in format: 00-000";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String zipPattern() default "\\d{2}-\\d{3}";
}
