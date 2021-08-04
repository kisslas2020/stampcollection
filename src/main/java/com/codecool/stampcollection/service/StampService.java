package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.StampNotFoundException;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.repository.StampRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StampService {

    private final StampRepository repository;

    public StampService(StampRepository repository) {
        this.repository = repository;
    }

    public List<Stamp> findAll() {
        return repository.findAll();
    }

    public Stamp findById(Long id) {
        Stamp stamp = repository.findById(id)
                .orElseThrow(() -> new StampNotFoundException(id));
        return stamp;
    }

    public List<Stamp> findAllByCountry(String country) {
        return repository.findAllByCountry(country);
    }

    public List<Stamp> findAllByYear(Integer year) {
        return repository.findAllByYearOfIssue(year);
    }

    public List<Stamp> findAllByCountryAndYear(String country, Integer year) {
        return repository.findAllByCountryAndYearOfIssue(country, year);
    }

    public Stamp addNew(Stamp stamp) {
        return repository.save(stamp);
    }

    public void deleteById(Long id) {
        Stamp stamp = repository.findById(id).orElseThrow(() -> new StampNotFoundException(id));
        if (stamp.getDenominations().size() == 0) {
            repository.deleteById(id);
        } else {
            throw new UnsupportedOperationException("you cannot delete stamp with active denominations");
        }
    }
}
