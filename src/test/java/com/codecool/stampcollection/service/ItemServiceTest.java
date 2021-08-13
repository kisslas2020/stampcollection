package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.exception.ItemNotFoundException;
import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.codecool.stampcollection.model.*;
import com.codecool.stampcollection.repository.DenominationRepository;
import com.codecool.stampcollection.repository.ItemRepository;
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
class ItemServiceTest {

    @Mock
    private ItemRepository repository;
    @Mock
    private DenominationRepository denominationRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ItemService service;

    private Item item;
    private Denomination denomination;
    private Transaction transactionBuy;
    private Transaction transactionSell;

    @BeforeEach
    void init() {
        Stamp stamp = new Stamp(1L, "butterflies", "HUN", 2010, new HashSet<Denomination>());
        denomination = new Denomination(1L, 10.0, Currency.getInstance("HUF"), stamp, 10L);
        transactionBuy = new Transaction(1L, LocalDate.of(2010, 10, 20), TransactionType.BUY, new ArrayList<Item>());
        transactionSell = new Transaction(1L, LocalDate.of(2010, 10, 20), TransactionType.SELL, new ArrayList<Item>());
        item = new Item(1L, denomination, 15L, 100.0, transactionBuy);
    }

    @Test
    void findById() {
        when(repository.findById(any())).thenReturn(Optional.of(item));
        assertEquals(item.getId(), service.findById(1L).getId());
    }

    @Test
    void findByIdWhenNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findAll() {
        List<Item> items = List.of(item);
        when(repository.findAll()).thenReturn(items);
        assertEquals(items.size(), service.findAll().size());
    }

    @Test
    void addNew() {
        when(denominationRepository.findById(any())).thenReturn(Optional.of(denomination));
        when(transactionRepository.findById(any())).thenReturn(Optional.of(transactionBuy));
        when(repository.save(any())).thenReturn(item);
        assertEquals(item.getId(), service.addNew(item).getId());
    }

    @Test
    void addNewWhenDenominationDoesNotExist() {
        when(denominationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(DenominationNotFoundException.class, () -> service.addNew(item));
    }

    @Test
    void addNewWhenTransactionDoesNotExist() {
        when(denominationRepository.findById(any())).thenReturn(Optional.of(denomination));
        when(transactionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> service.addNew(item));
    }

    @Test
    void addNewWhenSellLessThanHave() {
        item.setTransaction(transactionSell);
        when(denominationRepository.findById(any())).thenReturn(Optional.of(denomination));
        when(transactionRepository.findById(any())).thenReturn(Optional.of(transactionSell));
        assertThrows(UnsupportedOperationException.class, () -> service.addNew(item));
    }

    @Test
    void addNewWhenSellMoreThanHave() {
        item.setTransaction(transactionSell);
        item.setQuantity(15L);
        when(denominationRepository.findById(any())).thenReturn(Optional.of(denomination));
        when(transactionRepository.findById(any())).thenReturn(Optional.of(transactionSell));
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> service.addNew(item));
        assertEquals("Cannot sell more than have", exception.getMessage());
    }

    @Test
    void deleteById() {
        item.setQuantity(item.getDenomination().getStock());
        when(repository.findById(any())).thenReturn(Optional.of(item));
        service.deleteById(1L);
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    void deleteByIdWhenNotLastItem() {
        when(repository.countAllByIdIsGreaterThan(any())).thenReturn(1L);
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> service.deleteById(1L));
        assertEquals("Only the last item can be deleted", exception.getMessage());
    }
}