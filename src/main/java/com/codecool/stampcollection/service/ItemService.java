package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.exception.ItemNotFoundException;
import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Item;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.model.TransactionType;
import com.codecool.stampcollection.repository.DenominationRepository;
import com.codecool.stampcollection.repository.ItemRepository;
import com.codecool.stampcollection.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repository;
    private final DenominationRepository denominationRepository;
    private final TransactionRepository transactionRepository;

    public ItemService(ItemRepository repository, DenominationRepository denominationRepository, TransactionRepository transactionRepository) {
        this.repository = repository;
        this.denominationRepository = denominationRepository;
        this.transactionRepository = transactionRepository;
    }

    public Item findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public List<Item> findAll() {
        return repository.findAll();
    }

    public Item addNew(Item item) {
        Long denomId = item.getDenomination().getId();
        Long transId = item.getTransaction().getId();
        Denomination denomination = denominationRepository.findById(denomId)
                .orElseThrow(() -> new DenominationNotFoundException(denomId));
        Transaction transaction = transactionRepository.findById(transId)
                .orElseThrow(() -> new TransactionNotFoundException(transId));
        TransactionType transactionType = transaction.getTransactionType();
        Long quantity = item.getQuantity();
        Long signedQuantity = transactionType == TransactionType.BUY ? quantity : quantity * -1;
        updateDenomination(denomination, signedQuantity);
        return repository.save(item);
    }

    public void deleteById(Long id) {
        if (repository.countAllByIdIsGreaterThan(id) != 0) {
            throw new UnsupportedOperationException("Only the last item can be deleted");
        }
        Item item = repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        Denomination denomination = item.getDenomination();
        Transaction transaction = item.getTransaction();
        TransactionType transactionType = transaction.getTransactionType();
        Long quantity = item.getQuantity();
        Long signedQuantity = transactionType == TransactionType.BUY ? quantity * -1 : quantity;
        updateDenomination(denomination, signedQuantity);
        repository.deleteById(id);
    }

    private void updateDenomination(Denomination denomination, Long signedQuantity) {
        Long stock = denomination.getStock();
        if (stock + signedQuantity < 0) {
            throw new UnsupportedOperationException("Cannot sell more than have");
        }
        denomination.setStock(stock + signedQuantity);
        denominationRepository.save(denomination);
    }
}
