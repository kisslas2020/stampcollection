package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.codecool.stampcollection.model.*;
import com.codecool.stampcollection.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    private Transaction transaction;
    private Item item;
    private Denomination denomination;
    private Stamp stamp;

    @BeforeEach
    void init() {
        transaction = new Transaction(1L, LocalDate.of(2000, 10, 20), TransactionType.BUY, new ArrayList<Item>());
        stamp = new Stamp(1L, "butterflies", "HUN", 2010, new HashSet<Denomination>());
        denomination = new Denomination(1L, 10.0, Currency.getInstance("HUF"), stamp, 5L);
        item = new Item(1L, denomination, 10L, 100.0, transaction);
    }

    @Test
    void findById() {
        when(repository.findById(any())).thenReturn(Optional.of(transaction));
        assertEquals(transaction.getId(), service.findById(1L).getId());
    }

    @Test
    void findByIdWhenNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findAll() {
        List<Transaction> transactions = List.of(transaction);
        when(repository.findAll()).thenReturn(transactions);
        assertEquals(transactions.size(), service.findAll().size());
    }

    @Test
    void addNew() {
        when(repository.save(any())).thenReturn(transaction);
        assertEquals(transaction.getId(), service.addNew(transaction).getId());
    }

    @Test
    void deleteById() {
        when(repository.findById(any())).thenReturn(Optional.of(transaction));
        service.deleteById(1L);
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    void deleteByIdWhenHasItem() {
        transaction.setItems(List.of(item));
        when(repository.findById(any())).thenReturn(Optional.of(transaction));
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> service.deleteById(1L));
        assertEquals("Cannot delete transaction that has items in it", exception.getMessage());
    }
}