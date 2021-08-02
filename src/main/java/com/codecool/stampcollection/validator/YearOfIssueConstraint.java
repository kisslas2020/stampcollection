package com.codecool.stampcollection.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearOfIssueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface YearOfIssueConstraint {

    String message() default "Invalid year";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
