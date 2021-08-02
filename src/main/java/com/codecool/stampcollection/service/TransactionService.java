package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.repository.DenominationRepository;
import com.codecool.stampcollection.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final DenominationRepository denominationRepository;

    public TransactionService(TransactionRepository repository, DenominationRepository denominationRepository) {
        this.repository = repository;
        this.denominationRepository = denominationRepository;
    }

    public Transaction one(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return transaction;
    }

    public List<Transaction> all() {
        return repository.findAll();
    }

    public Transaction save(Transaction transaction) {
        Long denomId = transaction.getDenomId();
        Denomination denomination = denominationRepository.findById(denomId)
                .orElseThrow(() -> new UnsupportedOperationException("non-existent denominations cannot be purchased"));
        Long signedQuantity = getSignedQuantity(transaction);
        Long stock = denomination.getStock();
        if (stock + signedQuantity < 0) {
            throw new UnsupportedOperationException("Cannot sell more than you have");
        }
        denomination.setStock(stock + signedQuantity);
        denominationRepository.save(denomination);
        return repository.save(transaction);
    }

    public void delete(Long id) {
        Transaction transaction = repository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        Long denomId = transaction.getDenomId();
        Denomination denomination = denominationRepository.findById(denomId)
                .orElseThrow(() -> new UnsupportedOperationException("non-existent denominations cannot be purchased"));
        Long signedQuantity = getSignedQuantity(transaction);
        Long stock = denomination.getStock();
        if (stock - signedQuantity < 0) {
            throw new UnsupportedOperationException("Cannot delete this transaction");
        }
        denomination.setStock(stock - signedQuantity);
        denominationRepository.save(denomination);
        repository.deleteById(id);
    }

    private Long getSignedQuantity(Transaction transaction) {
        Long signedQuantity = 0L;
        switch (transaction.getTransactionType()) {
            case BUY:
                signedQuantity = transaction.getQuantity();
            case SELL:
                signedQuantity = transaction.getQuantity() * (-1);
        }
        return signedQuantity;
    }

}
