package com.codecool.stampcollection.assembler;

import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.DenominationDTO;
import com.codecool.stampcollection.controller.DenominationController;
import com.codecool.stampcollection.model.Denomination;
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
public class DenominationModelAssembler implements RepresentationModelAssembler<Denomination, EntityModel<DenominationDTO>> {

    private final MyModelMapper myModelMapper;

    public DenominationModelAssembler(MyModelMapper myModelMapper) {
        this.myModelMapper = myModelMapper;
    }

    @Override
    public EntityModel<DenominationDTO> toModel(Denomination entity) {
        DenominationDTO denominationDTO = myModelMapper.entityToDto(entity);
        return EntityModel.of(denominationDTO,
                linkTo(methodOn(DenominationController.class).findById(entity.getId())).withSelfRel(),
                linkTo(methodOn(DenominationController.class).findAll()).withRel("denominations"));
    }

    @Override
    public CollectionModel<EntityModel<DenominationDTO>> toCollectionModel(Iterable<? extends Denomination> entities) {
        List<EntityModel<DenominationDTO>> denominations = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(denominations, linkTo(methodOn(DenominationController.class).findAll()).withSelfRel());
    }
}
