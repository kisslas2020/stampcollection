package com.codecool.stampcollection.service;

import com.codecool.stampcollection.DTO.TransactionDTO;
import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final ModelMapper modelMapper;

    public TransactionService(TransactionRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public TransactionDTO one(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return convertToDto(transaction);
    }

    public List<TransactionDTO> all() {
        return repository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TransactionDTO save(TransactionDTO transactionDTO) {
        Transaction transaction = convertToEntity(transactionDTO);
        return convertToDto(repository.save(transaction));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private TransactionDTO convertToDto(Transaction transaction) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        return transactionDTO;
    }

    private Transaction convertToEntity(TransactionDTO transactionDTO) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        return transaction;
    }
}
