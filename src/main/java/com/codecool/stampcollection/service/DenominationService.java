package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.repository.DenominationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenominationService {

    private final DenominationRepository repository;

    public DenominationService(DenominationRepository repository) {
        this.repository = repository;
    }

    public Denomination findById(Long id) {
        Denomination denomination = repository.findById(id)
                .orElseThrow(() -> new DenominationNotFoundException(id));
        return denomination;
    }

    public List<Denomination> findAll() {
        return repository.findAll();
    }

    public Denomination addNew(Denomination denomination) {
        return repository.save(denomination);
    }

    public void deleteById(Long id) {
        Denomination denomination = repository.findById(id).orElseThrow(() -> new DenominationNotFoundException(id));
        if (denomination.getStock() == 0) {
            repository.deleteById(id);
        } else {
            throw new UnsupportedOperationException("Cannot delete denomination with activ stock value");
        }
    }

}
