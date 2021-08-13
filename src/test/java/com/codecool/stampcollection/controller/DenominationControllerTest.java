package com.codecool.stampcollection.controller;

import com.codecool.stampcollection.DTO.DenominationCommand;
import com.codecool.stampcollection.DTO.DenominationDTO;
import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.StampDTO;
import com.codecool.stampcollection.assembler.DenominationModelAssembler;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.StampService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.Currency;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DenominationControllerTest {

    @Mock
    private DenominationService denominationService;
    @Mock
    private StampService stampService;
    @Mock
    private DenominationModelAssembler assembler;
    @Mock
    private MyModelMapper modelMapper;

    @InjectMocks
    private DenominationController controller;

    private Stamp stamp;
    private Denomination denomination;
    private StampDTO stampDTO;
    private DenominationDTO denominationDTO;
    private DenominationCommand denominationCommand;
    private List<Denomination> denominations;
    private List<EntityModel<DenominationDTO>> entityModels;

    @BeforeEach
    void init() {
        stamp = new Stamp(1L,"butterflies","HUN",2010, new HashSet<Denomination>());
        stampDTO = new StampDTO(stamp.getId(), stamp.getName(), stamp.getCountry(), stamp.getYearOfIssue(), new HashSet<DenominationDTO>());
        denomination = new Denomination(1L, 10.0, Currency.getInstance("HUF"), stamp, 1L);
        denominationDTO = new DenominationDTO(denomination.getId(), denomination.getValue(), denomination.getCurrency(), denomination.getStamp().getId(), denomination.getStock());
        denominationCommand = new DenominationCommand(denomination.getValue(), denomination.getCurrency().getCurrencyCode(), denomination.getStamp().getId());
        denominations = List.of(denomination);
        entityModels = List.of(EntityModel.of(denominationDTO));

    }

    @Test
    void findById() {
        when(assembler.toModel(any())).thenReturn(EntityModel.of(denominationDTO));
        assertEquals(denomination.getId(), controller.findById(1L).getContent().getId());
        assertEquals(denomination.getStock(), controller.findById(1L).getContent().getStock());
        assertEquals(denomination.getValue(), controller.findById(1L).getContent().getValue());
        assertEquals(denomination.getCurrency(), controller.findById(1L).getContent().getCurrency());
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
    void addNew() {
        when(assembler.toModel(any())).thenReturn(EntityModel.of(denominationDTO));
        assertEquals(denomination.getId(), controller.addNew(denominationCommand).getContent().getId());
        assertEquals(denomination.getStock(), controller.addNew(denominationCommand).getContent().getStock());
        assertEquals(denomination.getValue(), controller.addNew(denominationCommand).getContent().getValue());
        assertEquals(denomination.getCurrency(), controller.addNew(denominationCommand).getContent().getCurrency());
    }

    @Test
    void update() {
        when(denominationService.findById(any())).thenReturn(denomination);
        when(stampService.findById(any())).thenReturn(stamp);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(denominationDTO));
        assertEquals(denomination.getId(), controller.update(1L, denominationCommand).getContent().getId());
        assertEquals(denomination.getStock(), controller.update(1L, denominationCommand).getContent().getStock());
        assertEquals(denomination.getValue(), controller.update(1L, denominationCommand).getContent().getValue());
        assertEquals(denomination.getCurrency(), controller.update(1L, denominationCommand).getContent().getCurrency());
        assertEquals(denomination.getStamp().getId(), controller.update(1L, denominationCommand).getContent().getStampId());
    }

    @Test
    void deleteById() {
        controller.deleteById(1L);
        verify(denominationService, times(1)).deleteById(1L);
    }
}