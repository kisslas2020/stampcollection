package com.codecool.stampcollection.assembler;

import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.controller.StampController;
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
public class StampModelAssembler implements RepresentationModelAssembler<StampDTO, EntityModel<StampDTO>> {

    @Override
    public EntityModel<StampDTO> toModel(StampDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(StampController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(StampController.class).all()).withRel("stamps"));
    }

    @Override
    public CollectionModel<EntityModel<StampDTO>> toCollectionModel(Iterable<? extends StampDTO> entities) {
        List<EntityModel<StampDTO>> stamps = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(stamps, linkTo(methodOn(StampController.class).all()).withSelfRel());
    }
}
