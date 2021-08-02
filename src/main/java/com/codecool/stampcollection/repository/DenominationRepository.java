package com.codecool.stampcollection.repository;

import com.codecool.stampcollection.model.Denomination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenominationRepository extends JpaRepository<Denomination, Long> {
}
