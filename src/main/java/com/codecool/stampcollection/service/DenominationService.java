package com.codecool.stampcollection.service;

import com.codecool.stampcollection.DTO.DenominationDTO;
import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.repository.DenominationRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DenominationService {

    private final DenominationRepository repository;
    private final ModelMapper modelMapper;

    public DenominationService(DenominationRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public DenominationDTO one(Long id) {
        Denomination denomination = repository.findById(id).orElseThrow(() -> new DenominationNotFoundException(id));
        return convertToDto(denomination);
    }

    public List<DenominationDTO> all() {
        return repository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DenominationDTO save(DenominationDTO denominationDTO) {
        Denomination denomination = convertToEntity(denominationDTO);
        return convertToDto(repository.save(denomination));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private DenominationDTO convertToDto(Denomination denomination) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper.map(denomination, DenominationDTO.class);
    }

    private Denomination convertToEntity(DenominationDTO denominationDTO) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper.map(denominationDTO, Denomination.class);
    }
}
