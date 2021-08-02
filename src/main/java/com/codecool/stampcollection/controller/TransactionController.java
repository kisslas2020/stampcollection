package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.TransactionDTO;
import com.codecool.stampcollection.assembler.TransactionModelAssembler;
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

    public TransactionController(TransactionService service, TransactionModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
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
        return assembler.toModel(service.save(transactionDTO));
    }

    @DeleteMapping("/{transaction_id}")
    public void delete(@PathVariable("transaction_id") Long id) {
        service.delete(id);
    }


}
