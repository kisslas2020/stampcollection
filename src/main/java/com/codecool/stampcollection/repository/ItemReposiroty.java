package com.codecool.stampcollection.repository;

import com.codecool.stampcollection.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemReposiroty extends JpaRepository<Item, Long> {

    Long countAllByIdIsGreaterThan(Long id);
}
