package com.codecool.stampcollection.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TransactionTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionTypeConstraint {

    String message() default "Transaction type can be 'BUY' or 'SELL'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
