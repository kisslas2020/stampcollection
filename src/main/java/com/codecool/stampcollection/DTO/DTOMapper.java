package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.StampService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DTOMapper {

    private final StampService stampService;

    public DTOMapper(StampService stampService) {
        this.stampService = stampService;
    }


    public StampDTO entityToDto(Stamp stamp) {
        StampDTO stampDTO = new StampDTO();
        stampDTO.setCountry(stamp.getCountry());
        stampDTO.setName(stamp.getName());
        stampDTO.setYearOfIssue(stamp.getYearOfIssue());
        stampDTO.setDenominations(stamp.getDenominations().stream()
                .map(d -> d.getValue())
                .collect(Collectors.toSet()));
        return stampDTO;
    }
    public Stamp dtoToEntity(StampDTO stampDTO) {
        Stamp stamp = new Stamp();
        stamp.setCountry(stampDTO.getCountry());
        stamp.setName(stampDTO.getName());
        stamp.setYearOfIssue(stampDTO.getYearOfIssue());
        return stamp;
    }

    public DenominationDTO entityToDto(Denomination denomination) {
        DenominationDTO denominationDTO = new DenominationDTO();
        denominationDTO.setCurrency(denomination.getCurrency());
        denominationDTO.setValue(denomination.getValue());
        denominationDTO.setStampId(denomination.getStamp().getId());
        return denominationDTO;
    }
    public Denomination dtoToEntity(DenominationDTO denominationDTO) {
        Denomination denomination = new Denomination();
        denomination.setCurrency(denominationDTO.getCurrency());
        denomination.setValue(denominationDTO.getValue());
        Stamp stamp = stampService.one(denominationDTO.getStampId());
        denomination.setStamp(stamp);
        return denomination;
    }

    public TransactionDTO entityToDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDateOfTransaction(transaction.getDateOfTransaction());
        transactionDTO.setTransactionType(transaction.getTransactionType());
        transactionDTO.setDenomId(transaction.getDenomId());
        transactionDTO.setQuantity(transaction.getQuantity());
        transactionDTO.setUnitPrice(transaction.getUnitPrice());
        return transactionDTO;
    }
    public Transaction dtoToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setDateOfTransaction(transactionDTO.getDateOfTransaction());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setDenomId(transactionDTO.getDenomId());
        transaction.setQuantity(transactionDTO.getQuantity());
        transaction.setUnitPrice(transactionDTO.getUnitPrice());
        return transaction;
    }
}
