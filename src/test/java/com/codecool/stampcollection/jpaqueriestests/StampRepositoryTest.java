package com.codecool.stampcollection.jpaqueriestests;

import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.repository.StampRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class StampRepositoryTest {

    @Autowired
    private StampRepository repository;

    @Test
    void injectedComponentNotNull() {
        assertThat(repository).isNotNull();
    }

    @Test
    void whenFindAllByCountry_withExistingCountry_thenReturnsList() {
        List<Stamp> expected = repository.findAll();
        List<Stamp> actual = repository.findAllByCountry("HUN");
        assertEquals(5, actual.size());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
    }

    @Test
    void whenFindAllByCountry_withNonExistingCountry_thenReturnsEmptyList() {
        List<Stamp> expected = repository.findAll();
        List<Stamp> actual = repository.findAllByCountry("USA");
        assertEquals(0, actual.size());
    }

    @Test
    void whenFindAllByYearOfIssue_withExistingYear_thenReturnsList() {
        List<Stamp> actual = repository.findAllByYearOfIssue(2020);
        assertEquals(1L, actual.get(0).getId());
        assertEquals("Butterflies", actual.get(0).getName());
    }

    @Test
    void whenFindAllByYearOfIssue_withNonExistingYear_thenReturnsList() {
        List<Stamp> actual = repository.findAllByYearOfIssue(2021);
        assertEquals(0, actual.size());
    }

    @Test
    void whenFindAllByCountryAndYearOfIssue_withExistingCountryAndYear_thenReturnsList() {
        List<Stamp> actual = repository.findAllByCountryAndYearOfIssue("HUN", 2020);
        assertEquals(1L, actual.get(0).getId());
        assertEquals("Butterflies", actual.get(0).getName());
    }

    @Test
    void whenFindAllByCountryAndYearOfIssue_withNonExistingCountryOrYear_thenReturnsList() {
        List<Stamp> actualNonExistingCountry = repository.findAllByCountryAndYearOfIssue("USA", 2020);
        List<Stamp> actualNonExistingYear = repository.findAllByCountryAndYearOfIssue("HUN", 2021);
        assertEquals(0, actualNonExistingCountry.size());
        assertEquals(0, actualNonExistingYear.size());
    }

    @Test
    void whenCountStampByCountryAndYearOfIssueAndName_withExisting_thenReturns1() {
        Long count = repository.countStampByCountryAndYearOfIssueAndName("HUN", 2020, "Butterflies");
        assertEquals(1, count);
    }

    @Test
    void whenCountStampByCountryAndYearOfIssueAndName_withNonExisting_thenReturns0() {
        Long countNonExistingCountry = repository.countStampByCountryAndYearOfIssueAndName("USA", 2020, "Butterflies");
        Long countNonExistingYear = repository.countStampByCountryAndYearOfIssueAndName("HUN", 2021, "Butterflies");
        Long countNonExistingName = repository.countStampByCountryAndYearOfIssueAndName("HUN", 2020, "Cats");
        assertEquals(0, countNonExistingCountry);
        assertEquals(0, countNonExistingYear);
        assertEquals(0, countNonExistingName);
    }

}
