package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.TransactionDTO;
import com.codecool.stampcollection.assembler.TransactionModelAssembler;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.service.TransactionService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/transaction")
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
    public EntityModel<TransactionDTO> one(@PathVariable("transaction_id") Long id) {
        return assembler.toModel(service.one(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<TransactionDTO>> all() {
        return assembler.toCollectionModel(service.all());
    }

    @PostMapping
    private EntityModel<TransactionDTO> save(@Valid @RequestBody TransactionDTO transactionDTO) {
        Transaction transaction = dtoMapper.dtoToEntity(transactionDTO);
        return assembler.toModel(service.save(transaction));
    }

    @DeleteMapping("/{transaction_id}")
    public void delete(@PathVariable("transaction_id") Long id) {
        service.delete(id);
    }


}