package com.codecool.stampcollection.repository;

import com.codecool.stampcollection.model.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRepository extends JpaRepository<Stamp, Long> {
}
