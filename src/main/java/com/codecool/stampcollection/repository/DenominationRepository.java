package com.codecool.stampcollection.repository;

import com.codecool.stampcollection.model.Denomination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Currency;

public interface DenominationRepository extends JpaRepository<Denomination, Long> {

    Long countDenominationByValueAndCurrencyAndStamp_Id(Double value, Currency currency, Long satmpId);
}
