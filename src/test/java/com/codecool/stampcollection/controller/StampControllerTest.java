package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DenominationDTO;
import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.StampCommand;
import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.assembler.StampModelAssembler;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.StampService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StampControllerTest {

    @Mock
    private StampService service;
    @Mock
    private StampModelAssembler assembler;
    @Mock
    private MyModelMapper myModelMapper;

    @InjectMocks
    private StampController controller;

    private Stamp stamp;
    private StampDTO stampDTO;
    private StampCommand stampCommand;
    private List<EntityModel<StampDTO>> entityModels;

    @BeforeEach
    void init() {
        stamp = new Stamp(1L, "butterflies", "HUN", 2010, new HashSet<Denomination>());
        stampDTO = new StampDTO(stamp.getId(), stamp.getName(), stamp.getCountry(), stamp.getYearOfIssue(), new HashSet<DenominationDTO>());
        stampCommand = new StampCommand(stamp.getName(), stamp.getCountry(), stamp.getYearOfIssue());
        entityModels = List.of(EntityModel.of(stampDTO));
    }

    @Test
    void findAll() {
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(entityModels));
        assertEquals(entityModels.size(), controller.findAll().getContent().size());
        assertEquals(entityModels.get(0)
                        .getContent()
                        .getId(),
                controller.findAll()
                        .getContent()
                        .stream()
                        .findFirst()
                        .get()
                        .getContent()
                        .getId());
    }

    @Test
    void findById() {
        when(assembler.toModel(any())).thenReturn(EntityModel.of(stampDTO));
        assertEquals(stamp.getId(), controller.findById(1L).getContent().getId());
        assertEquals(stamp.getCountry(), controller.findById(1L).getContent().getCountry());
        assertEquals(stamp.getName(), controller.findById(1L).getContent().getName());
        assertEquals(stamp.getYearOfIssue(), controller.findById(1L).getContent().getYearOfIssue());
        assertEquals(stamp.getDenominations().size(), controller.findById(1L).getContent().getDenominations().size());
    }

    @Test
    void findAllByCountry() {
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(entityModels));
        assertEquals(entityModels.size(), controller.findAllByCountry("HUN").getContent().size());
        assertEquals(entityModels.get(0)
                        .getContent()
                        .getId(),
                controller.findAllByCountry("HUN")
                        .getContent()
                        .stream()
                        .findFirst()
                        .get()
                        .getContent()
                        .getId());

    }

    @Test
    void findAllByYear() {
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(entityModels));
        assertEquals(entityModels.size(), controller.findAllByYear(2010).getContent().size());
        assertEquals(entityModels.get(0)
                        .getContent()
                        .getId(),
                controller.findAllByYear(2010)
                        .getContent()
                        .stream()
                        .findFirst()
                        .get()
                        .getContent()
                        .getId());
    }

    @Test
    void findAllByCountryAndYear() {
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(entityModels));
        assertEquals(entityModels.size(), controller.findAllByCountryAndYear("HUN", 2010).getContent().size());
        assertEquals(entityModels.get(0)
                        .getContent()
                        .getId(),
                controller.findAllByCountryAndYear("HUN", 2010)
                        .getContent()
                        .stream()
                        .findFirst()
                        .get()
                        .getContent()
                        .getId());
    }

    @Test
    void addNew() {
        when(myModelMapper.dtoToEntity(any(StampCommand.class))).thenReturn(stamp);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(stampDTO));
        assertEquals(stamp.getId(), controller.addNew(stampCommand).getContent().getId());
    }

    @Test
    void update() {
        when(service.findById(any())).thenReturn(stamp);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(stampDTO));
        assertEquals(stamp.getId(), controller.update(1L, stampCommand).getContent().getId());
    }

    @Test
    void deleteById() {
        controller.deleteById(1L);
        verify(service, times(1)).deleteById(1L);
    }
}