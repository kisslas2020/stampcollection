package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.assembler.StampModelAssembler;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.StampService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/stamp")
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
