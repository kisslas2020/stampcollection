package com.codecool.stampcollection.jpaqueriestests;

import com.codecool.stampcollection.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository repository;

    @Test
    void injectedComponentNotNull() {
        assertThat(repository).isNotNull();
    }

    @Test
    void whenCountAllByIdIsGreaterThan_withTheLastId_thenReturns0() {
        Long count = repository.countAllByIdIsGreaterThan(12L);
        assertEquals(0, count);
    }

    @Test
    void whenCountAllByIdIsGreaterThan_withTheLastButOneId_thenReturns1() {
        Long count = repository.countAllByIdIsGreaterThan(11L);
        assertEquals(1, count);
    }

}
