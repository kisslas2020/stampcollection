package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Item;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.StampService;
import com.codecool.stampcollection.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOMapper {

    private final StampService stampService;
    private final DenominationService denominationService;
    private final TransactionService transactionService;

    public DTOMapper(StampService stampService, DenominationService denominationService, TransactionService transactionService) {
        this.stampService = stampService;
        this.denominationService = denominationService;
        this.transactionService = transactionService;
    }


    public StampDTO entityToDto(Stamp stamp) {
        StampDTO stampDTO = new StampDTO();
        stampDTO.setId(stamp.getId());
        stampDTO.setCountry(stamp.getCountry());
        stampDTO.setName(stamp.getName());
        stampDTO.setYearOfIssue(stamp.getYearOfIssue());
        stampDTO.setDenominations(stamp.getDenominations().stream()
                .map(d -> d.getValue())
                .collect(Collectors.toSet()));
        return stampDTO;
    }

    public Stamp dtoToEntity(StampCommand command) {
        Stamp stamp = new Stamp();
        stamp.setCountry(command.getCountry());
        stamp.setName(command.getName());
        stamp.setYearOfIssue(command.getYearOfIssue());
        return stamp;
    }

    public DenominationDTO entityToDto(Denomination denomination) {
        DenominationDTO denominationDTO = new DenominationDTO();
        denominationDTO.setId(denomination.getId());
        denominationDTO.setCurrency(denomination.getCurrency());
        denominationDTO.setValue(denomination.getValue());
        denominationDTO.setStampId(denomination.getStamp().getId());
        denominationDTO.setStock(denomination.getStock());
        return denominationDTO;
    }
    public Denomination dtoToEntity(DenominationCommand command) {
        Denomination denomination = new Denomination();
        denomination.setCurrency(command.getCurrency());
        denomination.setValue(command.getValue());
        Stamp stamp = stampService.findById(command.getStampId());
        denomination.setStamp(stamp);
        return denomination;
    }

    public TransactionDTO entityToDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setDateOfTransaction(transaction.getDateOfTransaction());
        transactionDTO.setTransactionType(transaction.getTransactionType());
        List<ItemDTO> items = transaction.getItems().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        transactionDTO.setItemDTOList(items);
        return transactionDTO;
    }
    public Transaction dtoToEntity(TransactionCommand command) {
        Transaction transaction = new Transaction();
        transaction.setDateOfTransaction(command.getDateOfTransaction());
        transaction.setTransactionType(command.getTransactionType());
        return transaction;
    }

    public ItemDTO entityToDto(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setUnitPrice(item.getUnitPrice());
        itemDTO.setDenominationDTO(entityToDto(item.getDenomination()));
        itemDTO.setTransactionDTO(entityToDto(item.getTransaction()));
        return itemDTO;
    }

    public Item dtoToEntity(ItemCommand command) {
        Item item = new Item();
        item.setQuantity(command.getQuantity());
        item.setUnitPrice(command.getUnitPrice());
        Denomination denomination = denominationService.findById(command.getDenomId());
        item.setDenomination(denomination);
        Transaction transaction = transactionService.findById(command.getTransactionId());
        item.setTransaction(transaction);
        return item;
    }
}
