package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.ItemCommand;
import com.codecool.stampcollection.DTO.ItemDTO;
import com.codecool.stampcollection.assembler.ItemModelAssembler;
import com.codecool.stampcollection.exception.ItemNotFoundException;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Item;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.ItemService;
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
@RequestMapping("/api/item")
@Validated
@Api(tags = "Items")
public class ItemController {

    private final ItemService service;
    private final DenominationService denominationService;
    private final ItemModelAssembler assembler;
    private final DTOMapper dtoMapper;

    public ItemController(ItemService service, DenominationService denominationService, ItemModelAssembler assembler, DTOMapper dtoMapper) {
        this.service = service;
        this.denominationService = denominationService;
        this.assembler = assembler;
        this.dtoMapper = dtoMapper;
    }

    @ApiOperation(value = "View details of the selected item of a transaction")
    @GetMapping("/{item_id}")
    public EntityModel<ItemDTO> findById(@PathVariable("item_id") Long id) {
        return assembler.toModel(service.findById(id));
    }

    @ApiOperation(value = "View a list of items of all transactions")
    @GetMapping
    public CollectionModel<EntityModel<ItemDTO>> findAll() {
        return assembler.toCollectionModel(service.findAll());
    }

    @ApiOperation(value = "Create a new item to a transaction")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<ItemDTO> addNew(@Valid @RequestBody ItemCommand command) {
        Item item = dtoMapper.dtoToEntity(command);
        return assembler.toModel(service.addNew(item));
    }

    @ApiOperation(value = "Update an existing item")
    @PutMapping("/{item_id}")
    public EntityModel<ItemDTO> update(@PathVariable("item_id") Long id, @Valid @RequestBody ItemCommand command) {
        Item item = service.findById(id);
        item.setQuantity(command.getQuantity());
        item.setUnitPrice(command.getUnitPrice());
        Denomination denomination = denominationService.findById(command.getDenomId());
        item.setDenomination(denomination);
        return assembler.toModel(service.addNew(item));
    }

    @ApiOperation(value = "Delete the selected item",
            notes = "Only the last item can be deleted")
    @DeleteMapping("/{item_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("item_id") Long id) {
        service.deleteById(id);
    }

}
