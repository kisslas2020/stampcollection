package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.repository.DenominationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DenominationServiceTest {

    @Mock
    private DenominationRepository repository;

    @InjectMocks
    private DenominationService service;

    private Denomination denomination;
    private Stamp stamp;

    @BeforeEach
    void init() {
        stamp = new Stamp(1L, "butterflies", "HUN", 2010, new HashSet<Denomination>());
        denomination = new Denomination(1L, 10.0, Currency.getInstance("HUF"), stamp, 10L);
    }

    @Test
    void findById() {
        when(repository.findById(any())).thenReturn(Optional.of(denomination));
        assertEquals(denomination.getId(), service.findById(1L).getId());
    }

    @Test
    void findByIdWhenNotFound() {
        Long id = 2L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(DenominationNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void findAll() {
        List<Denomination> denominations = List.of(denomination);
        when(repository.findAll()).thenReturn(denominations);
        assertEquals(denominations.size(), service.findAll().size());
        assertEquals(denominations.get(0).getId(), service.findAll().get(0).getId());
    }

    @Test
    void addNew() {
        when(repository.save(any())).thenReturn(denomination);
        assertEquals(denomination.getId(), service.addNew(denomination).getId());
    }

    @Test
    void addNewWhenAlreadyExists() {
        when(repository.countDenominationByValueAndCurrencyAndStamp_Id(any(), any(), any())).thenReturn(1L);
        assertThrows(UnsupportedOperationException.class, () -> service.addNew(denomination));
    }

    @Test
    void deleteById() {
        denomination.setStock(0L);
        when(repository.findById(any())).thenReturn(Optional.of(denomination));
        service.deleteById(any());
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    void deleteByIdWhenHasStock() {
        when(repository.findById(any())).thenReturn(Optional.of(denomination));
        assertThrows(UnsupportedOperationException.class, () -> service.deleteById(1L));
    }
}