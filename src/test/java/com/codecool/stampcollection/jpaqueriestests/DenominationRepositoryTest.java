package com.codecool.stampcollection.jpaqueriestests;

import com.codecool.stampcollection.repository.DenominationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class DenominationRepositoryTest {

    @Autowired
    private DenominationRepository repository;

    @Test
    void injectedComponentNotNull() {
        assertThat(repository).isNotNull();
    }

    @Test
    void whenCountDenominationByValueAndCurrencyAndStamp_Id_withExistingDenomination_thenReturns1() {
        Long count = repository.countDenominationByValueAndCurrencyAndStamp_Id(10.0, Currency.getInstance("HUF"), 1L);
        assertEquals(1, count);
    }

    @Test
    void whenCountDenominationByValueAndCurrencyAndStamp_Id_withNotExistingDenomination_thenReturns0() {
        Long count = repository.countDenominationByValueAndCurrencyAndStamp_Id(10.0, Currency.getInstance("USD"), 1L);
        assertEquals(0, count);
    }

}
