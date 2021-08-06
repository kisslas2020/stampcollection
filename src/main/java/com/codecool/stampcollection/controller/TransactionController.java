package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.TransactionCommand;
import com.codecool.stampcollection.DTO.TransactionDTO;
import com.codecool.stampcollection.assembler.TransactionModelAssembler;
import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/transaction")
@Validated
@Api(tags = "Transactions")
public class TransactionController {

    private final TransactionService service;
    private final TransactionModelAssembler assembler;
    private final DTOMapper dtoMapper;

    public TransactionController(TransactionService service, TransactionModelAssembler assembler, DTOMapper dtoMapper) {
        this.service = service;
        this.assembler = assembler;
        this.dtoMapper = dtoMapper;
    }

    @ApiOperation(value = "View details of the selected transaction")
    @GetMapping("/{transaction_id}")
    public EntityModel<TransactionDTO> findById(@PathVariable("transaction_id") Long id) {
        return assembler.toModel(service.findById(id));
    }

    @ApiOperation(value = "View a list of all transactions")
    @GetMapping
    public CollectionModel<EntityModel<TransactionDTO>> findAll() {
        return assembler.toCollectionModel(service.findAll());
    }

    @ApiOperation(value = "Register a new transaction")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TransactionDTO> addNew(@Valid @RequestBody TransactionCommand command) {
        Transaction transaction = dtoMapper.dtoToEntity(command);
        return assembler.toModel(service.addNew(transaction));
    }

    @ApiOperation(value = "Update a registered transaction")
    @PutMapping("/{transaction_id}")
    public EntityModel<TransactionDTO> update(@PathVariable("transaction_id") Long id, @Valid @RequestBody TransactionCommand command) {
        Transaction transaction = service.findById(id);
        transaction.setTransactionType(command.getTransactionType());
        transaction.setDateOfTransaction(command.getDateOfTransaction());
        return assembler.toModel(service.addNew(transaction));
    }

    @ApiOperation(value = "Delete the selected transaction",
            notes = "It can only be deleted if it has no items in it")
    @DeleteMapping("/{transaction_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("transaction_id") Long id) {
        service.deleteById(id);
    }

}
