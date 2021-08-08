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

}
