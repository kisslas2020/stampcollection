package com.codecool.stampcollection.assembler;

import com.codecool.stampcollection.DTO.TransactionDTO;
import com.codecool.stampcollection.controller.TransactionController;
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
public class TransactionModelAssembler implements RepresentationModelAssembler<TransactionDTO, EntityModel<TransactionDTO>> {

    @Override
    public EntityModel<TransactionDTO> toModel(TransactionDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(TransactionController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(TransactionController.class).all()).withRel("transactions"));
    }

    @Override
    public CollectionModel<EntityModel<TransactionDTO>> toCollectionModel(Iterable<? extends TransactionDTO> entities) {
        List<EntityModel<TransactionDTO>> transactions = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(transactions, linkTo(methodOn(TransactionController.class).all()).withSelfRel());
    }
}
