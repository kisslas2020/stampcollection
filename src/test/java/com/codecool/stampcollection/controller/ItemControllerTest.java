package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.*;
import com.codecool.stampcollection.assembler.ItemModelAssembler;
import com.codecool.stampcollection.model.*;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.ItemService;
import com.codecool.stampcollection.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;
    @Mock
    private DenominationService denominationService;
    @Mock
    private ItemModelAssembler assembler;
    @Mock
    private MyModelMapper myModelMapper;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private ItemController controller;

    private Item item;
    private ItemDTO itemDTO;
    private ItemCommand itemCommand;
    private Denomination denomination;
    private DenominationDTO denominationDTO;
    private Transaction transaction;
    private TransactionDTO transactionDTO;
    private Stamp stamp;
    private StampDTO stampDTO;
    private List<EntityModel<ItemDTO>> entityModels;

    @BeforeEach
    void init() {
        stamp = new Stamp(1L,"butterflies","HUN",2010, new HashSet<Denomination>());
        stampDTO = new StampDTO(stamp.getId(), stamp.getName(), stamp.getCountry(), stamp.getYearOfIssue(), new HashSet<DenominationDTO>());
        denomination = new Denomination(1L, 10.0, Currency.getInstance("HUF"), stamp, 1L);
        denominationDTO = new DenominationDTO(denomination.getId(), denomination.getValue(), denomination.getCurrency(), denomination.getStamp().getId(), denomination.getStock());
        transaction = new Transaction(1L, LocalDate.of(2000, 3, 15), TransactionType.BUY, new ArrayList<>());
        transactionDTO = new TransactionDTO(transaction.getId(), transaction.getDateOfTransaction(), transaction.getTransactionType(), new ArrayList<>());
        item = new Item(1L, denomination, 5L, 100.0, transaction);
        itemDTO = new ItemDTO(item.getId(), denominationDTO, item.getQuantity(), item.getUnitPrice(), transactionDTO);
        itemCommand = new ItemCommand(item.getDenomination().getId(), item.getQuantity(), item.getUnitPrice(), item.getTransaction().getId());
        entityModels = List.of(EntityModel.of(itemDTO));
    }

    @Test
    void findById() {
        when(assembler.toModel(any())).thenReturn(EntityModel.of(itemDTO));
        assertEquals(item.getId(), controller.findById(1L).getContent().getId());
        assertEquals(item.getQuantity(), controller.findById(1L).getContent().getQuantity());
        assertEquals(item.getUnitPrice(), controller.findById(1L).getContent().getUnitPrice());
        assertEquals(item.getDenomination().getId(), controller.findById(1L).getContent().getDenominationDTO().getId());
        assertEquals(item.getTransaction().getId(), controller.findById(1L).getContent().getTransactionDTO().getId());
    }

    @Test
    void findAll() {
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(entityModels));
        assertEquals(entityModels.size(), controller.findAll().getContent().size());
        assertEquals(entityModels.get(0)
                        .getContent()
                        .getId(),
                controller.findAll()
                        .getContent()
                        .stream()
                        .findFirst()
                        .get()
                        .getContent()
                        .getId());
    }

    @Test
    void addNew() {
        when(assembler.toModel(any())).thenReturn(EntityModel.of(itemDTO));
        assertEquals(item.getId(), controller.addNew(itemCommand).getContent().getId());
    }

    @Test
    void update() {
        when(itemService.findById(any())).thenReturn(item);
        when(denominationService.findById(any())).thenReturn(denomination);
        when(transactionService.findById(any())).thenReturn(transaction);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(itemDTO));
        assertEquals(item.getId(), controller.update(1L, itemCommand).getContent().getId());
        assertEquals(item.getQuantity(), controller.update(1L, itemCommand).getContent().getQuantity());
        assertEquals(item.getTransaction().getId(), controller.update(1L, itemCommand).getContent().getTransactionDTO().getId());
        assertEquals(item.getUnitPrice(), controller.update(1L, itemCommand).getContent().getUnitPrice());
        assertEquals(item.getTransaction().getId(), controller.update(1L, itemCommand).getContent().getTransactionDTO().getId());
    }

    @Test
    void deleteById() {
        controller.deleteById(1L);
        verify(itemService, times(1)).deleteById(1L);
    }
}