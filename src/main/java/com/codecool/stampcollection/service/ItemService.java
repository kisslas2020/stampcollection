package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.exception.ItemNotFoundException;
import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.codecool.stampcollection.model.Item;
import com.codecool.stampcollection.repository.DenominationRepository;
import com.codecool.stampcollection.repository.ItemReposiroty;
import com.codecool.stampcollection.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemReposiroty reposiroty;
    private final DenominationRepository denominationRepository;
    private final TransactionRepository transactionRepository;

    public ItemService(ItemReposiroty reposiroty, DenominationRepository denominationRepository, TransactionRepository transactionRepository) {
        this.reposiroty = reposiroty;
        this.denominationRepository = denominationRepository;
        this.transactionRepository = transactionRepository;
    }

    public Item findById(Long id) {
        return reposiroty.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public List<Item> findAll() {
        return reposiroty.findAll();
    }

    public Item addNew(Item item) {
        Long denomId = item.getDenomination().getId();
        Long transId = item.getTransaction().getId();
        denominationRepository.findById(denomId).orElseThrow(() -> new DenominationNotFoundException(denomId));
        transactionRepository.findById(transId).orElseThrow(() -> new TransactionNotFoundException(transId));
        return reposiroty.save(item);
    }

    public void deleteById(Long id) {
        if (reposiroty.countAllByIdIsGreaterThan(id) != 0) {
            throw new UnsupportedOperationException("Only the last item can be deleted");
        }
        reposiroty.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        reposiroty.deleteById(id);
    }
}
