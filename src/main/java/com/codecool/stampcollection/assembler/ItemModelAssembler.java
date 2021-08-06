package com.codecool.stampcollection.assembler;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.ItemDTO;
import com.codecool.stampcollection.controller.ItemController;
import com.codecool.stampcollection.model.Item;
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
public class ItemModelAssembler implements RepresentationModelAssembler<Item, EntityModel<ItemDTO>> {

    private final DTOMapper dtoMapper;

    public ItemModelAssembler(DTOMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
    }

    @Override
    public EntityModel<ItemDTO> toModel(Item entity) {
        ItemDTO itemDTO = dtoMapper.entityToDto(entity);
        return EntityModel.of(itemDTO,
                linkTo(methodOn(ItemController.class).findById(entity.getId())).withSelfRel(),
                linkTo(methodOn(ItemController.class).findAll()).withRel("stamps"));
    }

    @Override
    public CollectionModel<EntityModel<ItemDTO>> toCollectionModel(Iterable<? extends Item> entities) {
        List<EntityModel<ItemDTO>> items = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(items, linkTo(methodOn(ItemController.class).findAll()).withSelfRel());
    }
}
