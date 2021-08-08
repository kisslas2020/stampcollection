package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction findById(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return transaction;
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Transaction addNew(Transaction transaction) {
        return repository.save(transaction);
    }

    public void deleteById(Long id) {
        Transaction transaction = findById(id);
        if (transaction.getItems().size() > 0) {
            throw new UnsupportedOperationException("Cannot delete transaction that has items in it");
        }
        repository.deleteById(id);
    }



    /*public Transaction addNew(Transaction transaction) {
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

    public void deleteById(Long id) {
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
                break;
            case SELL:
                signedQuantity = transaction.getQuantity() * (-1);
        }
        return signedQuantity;
    }*/

}
