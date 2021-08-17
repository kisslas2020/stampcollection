package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.model.*;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.StampService;
import com.codecool.stampcollection.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Currency;
import java.util.stream.Collectors;

@Component
public class MyModelMapper {

    private final ModelMapper modelMapper;
    private final StampService stampService;
    private final DenominationService denominationService;
    private final TransactionService transactionService;

    public MyModelMapper(ModelMapper modelMapper, StampService stampService, DenominationService denominationService, TransactionService transactionService) {
        this.modelMapper = modelMapper;
        this.stampService = stampService;
        this.denominationService = denominationService;
        this.transactionService = transactionService;
    }

    public StampDTO entityToDto(Stamp stamp) {
        StampDTO stampDTO = modelMapper.map(stamp, StampDTO.class);
        return stampDTO;
    }

    public Stamp dtoToEntity(StampCommand command) {
        Stamp stamp = modelMapper.map(command, Stamp.class);
        return stamp;
    }

    public DenominationDTO entityToDto(Denomination denomination) {
        DenominationDTO denominationDTO = modelMapper.map(denomination, DenominationDTO.class);
        return denominationDTO;
    }
    public Denomination dtoToEntity(DenominationCommand command) {
        Denomination denomination = new Denomination();
        denomination.setCurrency(Currency.getInstance(command.getCurrency()));
        denomination.setStock(0L);
        denomination.setValue(command.getValue());
        denomination.setStamp(stampService.findById(command.getStampId()));
        return denomination;
    }

    public TransactionDTO entityToDto(Transaction transaction) {
        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setItemDTOList(transaction.getItems().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList()));
        return transactionDTO;
    }
    public Transaction dtoToEntity(TransactionCommand command) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.valueOf(command.getTransactionType()));
        transaction.setDateOfTransaction(command.getDateOfTransaction());
        transaction.setItems(new ArrayList<>());
        return transaction;
    }

    public ItemDTO entityToDto(Item item) {
        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);
        return itemDTO;
    }

    public Item dtoToEntity(ItemCommand command) {
        Item item = new Item();
        item.setQuantity(command.getQuantity());
        item.setUnitPrice(command.getUnitPrice());
        Denomination denomination = denominationService.findById(command.getDenominationId());
        Transaction transaction = transactionService.findById(command.getTransactionId());
        item.setDenomination(denomination);
        item.setTransaction(transaction);
        return item;
    }
}
