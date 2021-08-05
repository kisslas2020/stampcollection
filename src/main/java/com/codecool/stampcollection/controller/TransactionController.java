package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.TransactionCommand;
import com.codecool.stampcollection.DTO.TransactionDTO;
import com.codecool.stampcollection.assembler.TransactionModelAssembler;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.service.TransactionService;
import io.swagger.annotations.Api;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping("/{transaction_id}")
    public EntityModel<TransactionDTO> findById(@PathVariable("transaction_id") Long id) {
        return assembler.toModel(service.findById(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<TransactionDTO>> findAll() {
        return assembler.toCollectionModel(service.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TransactionDTO> addNew(@Valid @RequestBody TransactionCommand command) {
        Transaction transaction = dtoMapper.dtoToEntity(command);
        return assembler.toModel(service.addNew(transaction));
    }

    @PutMapping("/{transaction_id}")
    public EntityModel<TransactionDTO> update(@PathVariable("transaction_id") Long id, @Valid @RequestBody TransactionCommand command) {
        Transaction transaction = service.findById(id);
        transaction.setTransactionType(command.getTransactionType());
        transaction.setDateOfTransaction(command.getDateOfTransaction());
        transaction.setUnitPrice(command.getUnitPrice());
        transaction.setQuantity(command.getQuantity());
        transaction.setDenomId(command.getDenomId());
        return assembler.toModel(service.addNew(transaction));
    }

    @DeleteMapping("/{transaction_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("transaction_id") Long id) {
        service.deleteById(id);
    }


}
