package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.ItemCommand;
import com.codecool.stampcollection.DTO.ItemDTO;
import com.codecool.stampcollection.assembler.ItemModelAssembler;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Item;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.ItemService;
import com.codecool.stampcollection.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/item")
@Validated
@Api(tags = "Items")
public class ItemController {

    private final ItemService service;
    private final DenominationService denominationService;
    private final ItemModelAssembler assembler;
    private final MyModelMapper myModelMapper;
    private final TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    public ItemController(ItemService service, DenominationService denominationService, ItemModelAssembler assembler,
                          MyModelMapper myModelMapper, TransactionService transactionService) {
        this.service = service;
        this.denominationService = denominationService;
        this.assembler = assembler;
        this.myModelMapper = myModelMapper;
        this.transactionService = transactionService;
    }

    @ApiOperation(value = "View details of the selected item of a transaction")
    @GetMapping("/{item_id}")
    public EntityModel<ItemDTO> findById(@PathVariable("item_id") Long id) {
        log.info("GET request for querying the item with id: {}", id);
        return assembler.toModel(service.findById(id));
    }

    @ApiOperation(value = "View a list of items of all transactions")
    @GetMapping
    public CollectionModel<EntityModel<ItemDTO>> findAll() {
        log.info("GET request for querying all items in database");
        return assembler.toCollectionModel(service.findAll());
    }

    @ApiOperation(value = "Create a new item to a transaction")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<ItemDTO> addNew(@Valid @RequestBody ItemCommand command) {
        log.info("POST request for creating new item with denomination id: {}, quantity: {}, unit price: {} " +
                "and transaction id: {}", command.getDenominationId(), command.getQuantity(), command.getUnitPrice(),
                command.getTransactionId());
        Item item = myModelMapper.dtoToEntity(command);
        return assembler.toModel(service.addNew(item));
    }

    @ApiOperation(value = "Update an existing item")
    @PutMapping("/{item_id}")
    public EntityModel<ItemDTO> update(@PathVariable("item_id") Long id, @Valid @RequestBody ItemCommand command) {
        log.info("PUT request for updating item originally dennomination id: {}, quantity: {}, unit price: {} " +
                        "and transaction id: {}", command.getDenominationId(), command.getQuantity(),
                command.getUnitPrice(), command.getTransactionId());
        Item item = service.findById(id);
        item.setQuantity(command.getQuantity());
        item.setUnitPrice(command.getUnitPrice());
        Denomination denomination = denominationService.findById(command.getDenominationId());
        item.setDenomination(denomination);
        Transaction transaction = transactionService.findById(command.getTransactionId());
        item.setTransaction(transaction);
        return assembler.toModel(service.addNew(item));
    }

    @ApiOperation(value = "Delete the selected item",
            notes = "Only the last item can be deleted")
    @DeleteMapping("/{item_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("item_id") Long id) {
        log.info("Delete request for deleting the item with id: {}", id);
        service.deleteById(id);
    }

}
