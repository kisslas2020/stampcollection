package com.codecool.stampcollection.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Locale;

public class YearOfIssueValidator implements ConstraintValidator<YearOfIssueConstraint, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        Integer yearOfFirstIssue = 1840;
        Integer currentYear = LocalDate.now().getYear();
        return year >= yearOfFirstIssue && year <= currentYear;
    }
}
