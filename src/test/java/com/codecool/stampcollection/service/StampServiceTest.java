package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.StampNotFoundException;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.repository.StampRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StampServiceTest {

    @Mock
    private StampRepository repository;

    @InjectMocks
    private StampService service;

    private Stamp stamp;
    private Denomination denomination;


    @BeforeEach
    void init() {
        stamp = new Stamp(1L, "butterflies", "HUN", 2010, new HashSet<Denomination>());
        denomination = new Denomination(1L, 10.0, Currency.getInstance("HUF"), stamp, 10L);
    }

    @Test
    void findAll() {
        List<Stamp> stamps = List.of(stamp);
        when(repository.findAll()).thenReturn(stamps);
        assertEquals(stamps.size(), service.findAll().size());
    }

    @Test
    void findById() {
        when(repository.findById(any())).thenReturn(Optional.of(stamp));
        assertEquals(stamp.getId(), service.findById(1L).getId());
    }

    @Test
    void findByIdWhenNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThrows(StampNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findAllByCountry() {
        List<Stamp> stamps = List.of(stamp);
        when(repository.findAllByCountry(any())).thenReturn(stamps);
        assertEquals(stamps.size(), service.findAllByCountry("HUN").size());
    }

    @Test
    void findAllByYear() {
        List<Stamp> stamps = List.of(stamp);
        when(repository.findAllByYearOfIssue(any())).thenReturn(stamps);
        assertEquals(stamps.size(), service.findAllByYear(2010).size());
    }

    @Test
    void findAllByCountryAndYear() {
        List<Stamp> stamps = List.of(stamp);
        when(repository.findAllByCountryAndYearOfIssue(any(), any())).thenReturn(stamps);
        assertEquals(stamps.size(), service.findAllByCountryAndYear("HUN", 2010).size());
    }

    @Test
    void addNew() {
        when(repository.save(any())).thenReturn(stamp);
        assertEquals(stamp.getId(), service.addNew(stamp).getId());
    }

    @Test
    void addNewWhenAlreadyExists() {
        when(repository.countStampByCountryAndYearOfIssueAndName(any(), any(), any())).thenReturn(1L);
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> service.addNew(stamp));
        assertEquals("Stamp already exists", exception.getMessage());
    }

    @Test
    void deleteById() {
        when(repository.findById(any())).thenReturn(Optional.of(stamp));
        service.deleteById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByIdWhenHasDenomination() {
        stamp.setDenominations(Set.of(denomination));
        when(repository.findById(any())).thenReturn(Optional.of(stamp));
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> service.deleteById(1L));
        assertEquals("You cannot delete stamp with active denominations", exception.getMessage());
    }
}