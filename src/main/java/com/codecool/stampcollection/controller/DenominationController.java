package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.DenominationCommand;
import com.codecool.stampcollection.DTO.DenominationDTO;
import com.codecool.stampcollection.assembler.DenominationModelAssembler;
import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.StampService;
import io.swagger.annotations.Api;
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
@RequestMapping("/api/denomination")
@Validated
@Api(tags = "Denominations")
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
    public EntityModel<DenominationDTO> findById(@PathVariable("denom_id") Long id) {
        return assembler.toModel(service.findById(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<DenominationDTO>> findAll() {
        return assembler.toCollectionModel(service.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<DenominationDTO> addNew(@Valid @RequestBody DenominationCommand command) {
        Denomination denomination = dtoMapper.dtoToEntity(command);
        return assembler.toModel(service.addNew(denomination));
    }

    @PutMapping("/{denom_id}")
    public EntityModel<DenominationDTO> update(@PathVariable("denom_id") Long id, @Valid @RequestBody DenominationCommand command) {
        Denomination denomination = service.findById(id);
        denomination.setValue(command.getValue());
        denomination.setCurrency(command.getCurrency());
        Stamp stamp = stampService.findById(command.getStampId());
        denomination.setStamp(stamp);
        return assembler.toModel(service.addNew(denomination));
    }

    @DeleteMapping("/{denom_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("denom_id") Long id) {
        service.deleteById(id);
    }

    @ExceptionHandler(DenominationNotFoundException.class)
    private ResponseEntity<Problem> handleNotFound(DenominationNotFoundException dnfe) {
        Problem problem = Problem.builder()
                .withType(URI.create("/api/denomination/denomination-not-found"))
                .withTitle("not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail(dnfe.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
