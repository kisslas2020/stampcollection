package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.assembler.StampModelAssembler;
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

    public StampController(StampService service, StampModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
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
        return assembler.toModel(service.save(stampDTO));
    }

    @DeleteMapping("/{stamp_id}")
    public void delete(@PathVariable("stamp_id") Long id) {
        service.delete(id);
    }
}
