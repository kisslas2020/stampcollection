package com.codecool.stampcollection.webmvctests;

import com.codecool.stampcollection.DTO.DenominationCommand;
import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.assembler.DenominationModelAssembler;
import com.codecool.stampcollection.controller.DenominationController;
import com.codecool.stampcollection.model.Denomination;
import com.codecool.stampcollection.model.Stamp;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.StampService;
import com.codecool.stampcollection.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Currency;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DenominationController.class)
@Import({DenominationModelAssembler.class, MyModelMapper.class, ModelMapper.class})
public class DenominationControllerIT {

    @MockBean
    private DenominationService service;

    @MockBean
    private StampService stampService;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Denomination denomination;
    private DenominationCommand denominationCommand;
    private Stamp stamp;

    @BeforeEach
    void init() {
        stamp = new Stamp(1l, "butterflies", "HUN", 2020, new HashSet<Denomination>());
        denomination = new Denomination(1L, 10.0, Currency.getInstance("HUF"), stamp, 15L);
        denominationCommand = new DenominationCommand(10.0, "HUF", 1L);
    }

    @Test
    void whenCallFindAll_thenReturnsList() throws Exception {
        List<Denomination> denominations = List.of(denomination);
        when(service.findAll()).thenReturn(denominations);
        mockMvc.perform(get("/api/denomination"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.denominationDTOList.size()").value(denominations.size()))
                .andExpect(jsonPath("$._embedded.denominationDTOList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.denominationDTOList[0].value").value(10.0))
                .andExpect(jsonPath("$._embedded.denominationDTOList[0].currency").value("HUF"))
                .andExpect(jsonPath("$._embedded.denominationDTOList[0].stampId").value(1L))
                .andExpect(jsonPath("$._embedded.denominationDTOList[0].stock").value(15L))
                .andExpect(jsonPath("$._embedded.denominationDTOList[0]._links.self.href").value("http://localhost/api/denomination/1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/denomination"));
    }

    @Test
    void whenCallFindById_thenReturnsDenomination() throws Exception {
        when(service.findById(any())).thenReturn(denomination);
        mockMvc.perform(get("/api/denomination/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(10.0))
                .andExpect(jsonPath("$.currency").value("HUF"))
                .andExpect(jsonPath("$.stampId").value(1L))
                .andExpect(jsonPath("$.stock").value(15L))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/denomination/1"));
    }



    @Test
    void whenAddNew_withValidInput_thenReturnsSameObject() throws Exception {
        when(service.findById(any())).thenReturn(denomination);
        when(service.addNew(any())).thenReturn(denomination);
        mockMvc.perform(post("/api/denomination")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"value\": 10.0, \"currency\": \"HUF\", \"stampId\": 1 }"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(10.0))
                .andExpect(jsonPath("$.currency").value("HUF"))
                .andExpect(jsonPath("$.stampId").value(1L))
                .andExpect(jsonPath("$.stock").value(15L))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/denomination/1"));
    }

    @Test
    void whenAddNew_withNullValue_thenReturnsBadRequest() throws Exception {
        when(service.findById(any())).thenReturn(denomination);
        when(service.addNew(any())).thenReturn(denomination);
        mockMvc.perform(post("/api/denomination")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"value\": null, \"currency\": \"HUF\", \"stampId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenAddNew_withValidInput_thenTransformIntoService() throws Exception {
        when(service.findById(any())).thenReturn(denomination);
        when(service.addNew(any())).thenReturn(denomination);
        when(stampService.findById(any())).thenReturn(stamp);
        mockMvc.perform(post("/api/denomination")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denominationCommand)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Denomination> captor = ArgumentCaptor.forClass(Denomination.class);
        verify(service, times(1)).addNew(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals(10.0, captor.getValue().getValue());
        assertEquals(Currency.getInstance("HUF"), captor.getValue().getCurrency());
        assertEquals(stamp, captor.getValue().getStamp());
        assertEquals(0L, captor.getValue().getStock());
    }

    @Test
    void whenUpdate_withValidInput_thenReturnsUpdatedDenomination() throws Exception {
        when(service.findById(any())).thenReturn(denomination);
        when(service.addNew(any())).thenReturn(denomination);
        when(stampService.findById(any())).thenReturn(stamp);
        mockMvc.perform(put("/api/denomination/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"value\": 10.0, \"currency\": \"HUF\", \"stampId\": 1 }"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(10.0))
                .andExpect(jsonPath("$.currency").value("HUF"))
                .andExpect(jsonPath("$.stampId").value(1L))
                .andExpect(jsonPath("$.stock").value(15L))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/denomination/1"));
    }

    @Test
    void whenUpdate_withValidInput_thenTransformsIntoService() throws Exception {
        when(service.findById(any())).thenReturn(denomination);
        when(service.addNew(any())).thenReturn(denomination);
        when(stampService.findById(any())).thenReturn(stamp);
        mockMvc.perform(put("/api/denomination/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"value\": 10.0, \"currency\": \"HUF\", \"stampId\": 1 }"))
                .andExpect(status().isOk());

        ArgumentCaptor<Denomination> captor = ArgumentCaptor.forClass(Denomination.class);
        verify(service, times(1)).addNew(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals(10.0, captor.getValue().getValue());
        assertEquals(Currency.getInstance("HUF"), captor.getValue().getCurrency());
        assertEquals(stamp, captor.getValue().getStamp());
        assertEquals(15L, captor.getValue().getStock());
    }

    @Test
    void whenUpdate_withNullValue_thenReturnsBadRequest() throws Exception {
        when(service.findById(any())).thenReturn(denomination);
        when(service.addNew(any())).thenReturn(denomination);
        mockMvc.perform(put("/api/denomination/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"value\": null, \"currency\": \"HUF\", \"stampId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenDelete_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/denomination/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(any());
    }

}
