package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.DenominationCommand;
import com.codecool.stampcollection.DTO.DenominationDTO;
import com.codecool.stampcollection.assembler.DenominationModelAssembler;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.StampService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Currency;

@RestController
@RequestMapping("/api/denomination")
@Validated
@Api(tags = "Denominations")
public class DenominationController {

    private final DenominationService service;
    private final StampService stampService;
    private final DenominationModelAssembler assembler;
    private final MyModelMapper myModelMapper;

    public DenominationController(DenominationService service, StampService stampService,
                                  DenominationModelAssembler assembler, MyModelMapper myModelMapper) {
        this.service = service;
        this.stampService = stampService;
        this.assembler = assembler;
        this.myModelMapper = myModelMapper;
    }

    @ApiOperation(value = "View details of the selected denomination")
    @GetMapping("/{denom_id}")
    public EntityModel<DenominationDTO> findById(@PathVariable("denom_id") Long id) {
        return assembler.toModel(service.findById(id));
    }

    @ApiOperation(value = "View a list of possessed denominations")
    @GetMapping
    public CollectionModel<EntityModel<DenominationDTO>> findAll() {
        return assembler.toCollectionModel(service.findAll());
    }

    @ApiOperation(value = "Create a new denomination object")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<DenominationDTO> addNew(@Valid @RequestBody DenominationCommand command) {
        Denomination denomination = myModelMapper.dtoToEntity(command);
        return assembler.toModel(service.addNew(denomination));
    }

    @ApiOperation(value = "Update an existing denomination")
    @PutMapping("/{denom_id}")
    public EntityModel<DenominationDTO> update(@PathVariable("denom_id") Long id,
                                               @Valid @RequestBody DenominationCommand command) {
        Denomination denomination = service.findById(id);
        denomination.setValue(command.getValue());
        denomination.setCurrency(Currency.getInstance(command.getCurrency()));
        Stamp stamp = stampService.findById(command.getStampId());
        denomination.setStamp(stamp);
        return assembler.toModel(service.addNew(denomination));
    }

    @ApiOperation(value = "Delete the selected denomination from collection",
            notes = "It can only be deleted if its stock value equals zero")
    @DeleteMapping("/{denom_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("denom_id") Long id) {
        service.deleteById(id);
    }

}
