package com.codecool.stampcollection.assembler;

import com.codecool.stampcollection.DTO.DenominationDTO;
import com.codecool.stampcollection.controller.DenominationController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DenominationModelAssembler implements RepresentationModelAssembler<DenominationDTO, EntityModel<DenominationDTO>> {

    @Override
    public EntityModel<DenominationDTO> toModel(DenominationDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(DenominationController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(DenominationController.class).all()).withRel("denominations"));
    }

    @Override
    public CollectionModel<EntityModel<DenominationDTO>> toCollectionModel(Iterable<? extends DenominationDTO> entities) {
        List<EntityModel<DenominationDTO>> denominations = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(denominations, linkTo(methodOn(DenominationController.class).all()).withSelfRel());
    }
}
