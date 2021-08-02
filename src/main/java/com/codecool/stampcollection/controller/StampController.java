package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.assembler.StampModelAssembler;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.StampService;
import com.codecool.stampcollection.validator.YearOfIssueConstraint;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    public CollectionModel<EntityModel<StampDTO>> all() {
        return assembler.toCollectionModel(service.findAll());
    }

    @GetMapping("/{stamp_id}")
    public EntityModel<StampDTO> one(@PathVariable("stamp_id") Long id) {
        return assembler.toModel(service.one(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<StampDTO>> allByCountry(@RequestParam @NotBlank @Size(min = 3, max = 3,
            message = "Use three-letter Alpha-3 code.") String country) {
        return assembler.toCollectionModel(service.allByCountry(country));
    }

    @GetMapping
    public CollectionModel<EntityModel<StampDTO>> allByYear(@RequestParam Integer year) {
        return assembler.toCollectionModel(service.allByYear(year));
    }

    @GetMapping
    public CollectionModel<EntityModel<StampDTO>> allByCountryAndYear(@RequestParam @NotBlank @Size(min = 3, max = 3,
            message = "Use three-letter Alpha-3 code.") String country, @RequestParam Integer year) {
        return assembler.toCollectionModel(service.allByCountryAndYear(country, year));
    }

    @PostMapping
    public EntityModel<StampDTO> save(@Valid @RequestBody StampDTO stampDTO) {
        Stamp stamp = dtoMapper.dtoToEntity(stampDTO);
        return assembler.toModel(service.save(stamp));
    }

    @PutMapping("/{stamp_id}")
    public EntityModel<StampDTO> update(@PathVariable("stamp_id") Long id, @Valid @RequestBody StampDTO stampDTO) {
        Stamp stamp = service.one(id);
        stamp.setYearOfIssue(stampDTO.getYearOfIssue());
        stamp.setName(stampDTO.getName());
        stamp.setCountry(stampDTO.getCountry());
        return assembler.toModel(service.save(stamp));
    }

    @DeleteMapping("/{stamp_id}")
    public void delete(@PathVariable("stamp_id") Long id) {
        service.delete(id);
    }
}
