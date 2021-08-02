package com.codecool.stampcollection.assembler;

import com.codecool.stampcollection.DTO.DTOMapper;
import com.codecool.stampcollection.DTO.TransactionDTO;
import com.codecool.stampcollection.controller.TransactionController;
import com.codecool.stampcollection.model.Transaction;
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
public class TransactionModelAssembler implements RepresentationModelAssembler<Transaction, EntityModel<TransactionDTO>> {

    private final DTOMapper dtoMapper;

    public TransactionModelAssembler(DTOMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
    }

    @Override
    public EntityModel<TransactionDTO> toModel(Transaction entity) {
        TransactionDTO transactionDTO = dtoMapper.entityToDto(entity);
        return EntityModel.of(transactionDTO,
                linkTo(methodOn(TransactionController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(TransactionController.class).all()).withRel("transactions"));
    }

    @Override
    public CollectionModel<EntityModel<TransactionDTO>> toCollectionModel(Iterable<? extends Transaction> entities) {
        List<EntityModel<TransactionDTO>> transactions = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(transactions, linkTo(methodOn(TransactionController.class).all()).withSelfRel());
    }
}
