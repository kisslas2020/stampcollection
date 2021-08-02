package com.codecool.stampcollection.repository;

import com.codecool.stampcollection.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
