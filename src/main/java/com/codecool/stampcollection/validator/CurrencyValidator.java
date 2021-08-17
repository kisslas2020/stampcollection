package com.codecool.stampcollection.validator;

import com.codecool.stampcollection.exception.CurrencyNotExistsException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import java.util.Currency;
import java.util.Set;

public class CurrencyValidator implements ConstraintValidator<CurrencyConstraint, String> {


    @Override
    public boolean isValid(String currency, ConstraintValidatorContext constraintValidatorContext) {
        if (currency == null) {
            return false;
        }
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        try {
            return currencies.contains(Currency.getInstance(currency));
        } catch (IllegalArgumentException iae) {
            throw new CurrencyNotExistsException(currency);
        }
    }

}
