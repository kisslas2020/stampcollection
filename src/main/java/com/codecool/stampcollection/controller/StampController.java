package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.StampCommand;
import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.assembler.StampModelAssembler;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.StampService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;

@RestController
@RequestMapping("/api/stamp")
@Validated
@Api(tags = "Stamps")
public class StampController {

    private final StampService service;
    private final StampModelAssembler assembler;
    private final MyModelMapper myModelMapper;

    public StampController(StampService service, StampModelAssembler assembler, MyModelMapper myModelMapper) {
        this.service = service;
        this.assembler = assembler;
        this.myModelMapper = myModelMapper;
    }

    @ApiOperation(value = "View a list of possessed stamps")
    @GetMapping
    public CollectionModel<EntityModel<StampDTO>> findAll() {
        return assembler.toCollectionModel(service.findAll());
    }

    @ApiOperation(value = "View details of the selected stamp")
    @GetMapping("/{stamp_id}")
    public EntityModel<StampDTO> findById(@PathVariable("stamp_id") Long id) {
        return assembler.toModel(service.findById(id));
    }

    @ApiOperation(value = "View list of possessed stamps filtered by given country")
    @GetMapping("/country")
    public CollectionModel<EntityModel<StampDTO>> findAllByCountry(@RequestParam @NotBlank @Size(min = 3, max = 3,
            message = "Use three-letter Alpha-3 code.") String country) {
        return assembler.toCollectionModel(service.findAllByCountry(country));
    }

    @ApiOperation(value = "View list of possessed stamps filtered by given year")
    @GetMapping("/year")
    public CollectionModel<EntityModel<StampDTO>> findAllByYear(@RequestParam Integer year) {
        return assembler.toCollectionModel(service.findAllByYear(year));
    }

    @ApiOperation(value = "View list of possessed stamps filtered by given country and year")
    @GetMapping("/countryandyear")
    public CollectionModel<EntityModel<StampDTO>> findAllByCountryAndYear(@RequestParam @NotBlank @Size(min = 3, max = 3,
            message = "Use three-letter Alpha-3 code.") String country, @RequestParam Integer year) {
        return assembler.toCollectionModel(service.findAllByCountryAndYear(country, year));
    }

    @ApiOperation(value = "Create a new stamp object")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<StampDTO> addNew(@Valid @RequestBody StampCommand command) {
        Stamp stamp = myModelMapper.dtoToEntity(command);
        stamp.setDenominations(new HashSet<>());
        return assembler.toModel(service.addNew(stamp));
    }

    @ApiOperation(value = "Update an existing stamp")
    @PutMapping("/{stamp_id}")
    public EntityModel<StampDTO> update(@PathVariable("stamp_id") Long id, @Valid @RequestBody StampCommand command) {
        Stamp stamp = service.findById(id);
        stamp.setYearOfIssue(command.getYearOfIssue());
        stamp.setName(command.getName());
        stamp.setCountry(command.getCountry());
        return assembler.toModel(service.addNew(stamp));
    }

    @ApiOperation(value = "Delete the selected stamp from collection",
            notes = "It can only be deleted if its denominations have been deleted previously")
    @DeleteMapping("/{stamp_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("stamp_id") Long id) {
        service.deleteById(id);
    }

}
