package com.codecool.stampcollection.repository;

import com.codecool.stampcollection.model.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    List<Stamp> findAllByCountry(String country);

    List<Stamp> findAllByYearOfIssue(Integer year);

    List<Stamp> findAllByCountryAndYearOfIssue(String country, Integer year);
}
