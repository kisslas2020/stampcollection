package com.codecool.stampcollection.service;

import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.exception.StampNotFoundException;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.repository.StampRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StampService {

    private final StampRepository repository;
    private final ModelMapper modelMapper;

    public StampService(StampRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public List<StampDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StampDTO one(Long id) {
        Stamp stamp = repository.findById(id)
                .orElseThrow(() -> new StampNotFoundException(id));
        return convertToDto(stamp);
    }

    public StampDTO save(StampDTO stampDTO) {
        Stamp stamp = convertToEntity(stampDTO);
        return convertToDto(repository.save(stamp));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private StampDTO convertToDto(Stamp stamp) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper.map(stamp, StampDTO.class);
    }

    private Stamp convertToEntity(StampDTO stampDTO) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper.map(stampDTO, Stamp.class);
    }
}
