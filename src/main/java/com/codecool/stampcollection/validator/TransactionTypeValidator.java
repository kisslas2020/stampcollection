package com.codecool.stampcollection.validator;

import com.codecool.stampcollection.model.TransactionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class TransactionTypeValidator implements ConstraintValidator<TransactionTypeConstraint, String> {

    @Override
    public boolean isValid(String transactionType, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(TransactionType.values())
                .anyMatch(type -> type.getName().equals(transactionType));
    }

}
