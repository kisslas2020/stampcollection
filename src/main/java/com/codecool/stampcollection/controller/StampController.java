package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.StampCommand;
import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.assembler.StampModelAssembler;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.StampService;
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
public class StampController {

    private final StampService service;
    private final StampModelAssembler assembler;
    private final DTOMapper dtoMapper;

    public StampController(StampService service, StampModelAssembler assembler, DTOMapper dtoMapper) {
        this.service = service;
        this.assembler = assembler;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public CollectionModel<EntityModel<StampDTO>> findAll() {
        return assembler.toCollectionModel(service.findAll());
    }

    @GetMapping("/{stamp_id}")
    public EntityModel<StampDTO> findById(@PathVariable("stamp_id") Long id) {
        return assembler.toModel(service.findById(id));
    }

    @GetMapping("/country")
    public CollectionModel<EntityModel<StampDTO>> findAllByCountry(@RequestParam @NotBlank @Size(min = 3, max = 3,
            message = "Use three-letter Alpha-3 code.") String country) {
        return assembler.toCollectionModel(service.findAllByCountry(country));
    }

    @GetMapping("/year")
    public CollectionModel<EntityModel<StampDTO>> findAllByYear(@RequestParam Integer year) {
        return assembler.toCollectionModel(service.findAllByYear(year));
    }

    @GetMapping("/countryandyear")
    public CollectionModel<EntityModel<StampDTO>> findAllByCountryAndYear(@RequestParam @NotBlank @Size(min = 3, max = 3,
            message = "Use three-letter Alpha-3 code.") String country, @RequestParam Integer year) {
        return assembler.toCollectionModel(service.findAllByCountryAndYear(country, year));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<StampDTO> addNew(@Valid @RequestBody StampCommand command) {
        Stamp stamp = dtoMapper.dtoToEntity(command);
        stamp.setDenominations(new HashSet<>());
        return assembler.toModel(service.addNew(stamp));
    }

    @PutMapping("/{stamp_id}")
    public EntityModel<StampDTO> update(@PathVariable("stamp_id") Long id, @Valid @RequestBody StampCommand command) {
        Stamp stamp = service.findById(id);
        stamp.setYearOfIssue(command.getYearOfIssue());
        stamp.setName(command.getName());
        stamp.setCountry(command.getCountry());
        return assembler.toModel(service.addNew(stamp));
    }

    @DeleteMapping("/{stamp_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("stamp_id") Long id) {
        service.deleteById(id);
    }
}
