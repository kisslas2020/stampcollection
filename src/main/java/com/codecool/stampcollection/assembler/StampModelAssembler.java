package com.codecool.stampcollection.assembler;

import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.controller.StampController;
import com.codecool.stampcollection.model.Stamp;
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
public class StampModelAssembler implements RepresentationModelAssembler<Stamp, EntityModel<StampDTO>> {

    private final MyModelMapper myModelMapper;

    public StampModelAssembler(MyModelMapper myModelMapper) {
        this.myModelMapper = myModelMapper;
    }

    @Override
    public EntityModel<StampDTO> toModel(Stamp entity) {
        StampDTO stampDTO = myModelMapper.entityToDto(entity);
        return EntityModel.of(stampDTO,
                linkTo(methodOn(StampController.class).findById(entity.getId())).withSelfRel(),
                linkTo(methodOn(StampController.class).findAll()).withRel("stamps"));
    }

    @Override
    public CollectionModel<EntityModel<StampDTO>> toCollectionModel(Iterable<? extends Stamp> entities) {
        List<EntityModel<StampDTO>> stamps = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(stamps, linkTo(methodOn(StampController.class).findAll()).withSelfRel());
    }
}
