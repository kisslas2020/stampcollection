package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.DenominationDTO;
import com.codecool.stampcollection.assembler.DenominationModelAssembler;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.StampService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/denomination")
public class DenominationController {

    private final DenominationService service;
    private final StampService stampService;
    private final DenominationModelAssembler assembler;
    private final DTOMapper dtoMapper;

    public DenominationController(DenominationService service, StampService stampService, DenominationModelAssembler assembler, DTOMapper dtoMapper) {
        this.service = service;
        this.stampService = stampService;
        this.assembler = assembler;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/{denom_id}")
    public EntityModel<DenominationDTO> one(@PathVariable("denom_id") Long id) {
        return assembler.toModel(service.one(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<DenominationDTO>> all() {
        return assembler.toCollectionModel(service.all());
    }

    @PostMapping
    public EntityModel<DenominationDTO> save(@Valid @RequestBody DenominationDTO denominationDTO) {
        Denomination denomination = dtoMapper.dtoToEntity(denominationDTO);
        return assembler.toModel(service.save(denomination));
    }

    @PutMapping("/{denom_id}")
    public EntityModel<DenominationDTO> update(@PathVariable("denom_id") Long id, @Valid @RequestBody DenominationDTO denominationDTO) {
        Denomination denomination = service.one(id);
        denomination.setValue(denominationDTO.getValue());
        denomination.setCurrency(denominationDTO.getCurrency());
        Stamp stamp = stampService.one(denominationDTO.getStampId());
        denomination.setStamp(stamp);
        return assembler.toModel(service.save(denomination));
    }

    @DeleteMapping("/{denom_id}")
    public void delete(@PathVariable("denom_id") Long id) {
        service.delete(id);
    }


}
