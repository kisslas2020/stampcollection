package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.ItemDTO;
import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.TransactionCommand;
import com.codecool.stampcollection.DTO.TransactionDTO;
import com.codecool.stampcollection.assembler.TransactionModelAssembler;
import com.codecool.stampcollection.model.Item;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.model.TransactionType;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService service;
    @Mock
    private TransactionModelAssembler assembler;
    @Mock
    private MyModelMapper myModelMapper;

    @InjectMocks
    private TransactionController controller;

    private Transaction transaction;
    private TransactionDTO transactionDTO;
    private TransactionCommand transactionCommand;
    private List<EntityModel<TransactionDTO>> entityModels;

    @BeforeEach
    void init() {
        transaction = new Transaction(1L, LocalDate.of(2000, 10, 20), TransactionType.BUY, new ArrayList<Item>());
        transactionDTO = new TransactionDTO(1L, transaction.getDateOfTransaction(), transaction.getTransactionType(), new ArrayList<ItemDTO>());
        transactionCommand = new TransactionCommand(transaction.getDateOfTransaction(), transaction.getTransactionType().getName());
        entityModels = List.of(EntityModel.of(transactionDTO));
    }

    @Test
    void findById() {
        when(assembler.toModel(any())).thenReturn(EntityModel.of(transactionDTO));
        assertEquals(transaction.getId(), controller.findById(1L).getContent().getId());
        assertEquals(transaction.getTransactionType(), controller.findById(1L).getContent().getTransactionType());
        assertEquals(transaction.getDateOfTransaction(), controller.findById(1L).getContent().getDateOfTransaction());
        assertEquals(transaction.getItems().size(), controller.findById(1L).getContent().getItemDTOList().size());
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
        when(assembler.toModel(any())).thenReturn(EntityModel.of(transactionDTO));
        assertEquals(transaction.getId(), controller.addNew(transactionCommand).getContent().getId());
    }

    @Test
    void update() {
        when(service.findById(any())).thenReturn(transaction);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(transactionDTO));
        assertEquals(transaction.getId(), controller.update(1L, transactionCommand).getContent().getId());
    }

    @Test
    void deleteById() {
        controller.deleteById(1L);
        verify(service, times(1)).deleteById(1L);
    }
}