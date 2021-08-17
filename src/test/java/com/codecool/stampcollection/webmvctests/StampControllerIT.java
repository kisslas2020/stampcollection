package com.codecool.stampcollection.webmvctests;

import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.StampCommand;
import com.codecool.stampcollection.assembler.StampModelAssembler;
import com.codecool.stampcollection.controller.StampController;
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

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StampController.class)
@Import({StampModelAssembler.class, MyModelMapper.class, ModelMapper.class})
public class StampControllerIT {

    @MockBean
    private StampService service;

    @MockBean
    private DenominationService denominationService;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Stamp stamp;
    private StampCommand stampCommand;

    @BeforeEach
    void init() {
        stamp = new Stamp(1L, "butterflies", "HUN", 2020, new HashSet<Denomination>());
        stampCommand = new StampCommand("butterflies", "HUN", 2020);
    }

    @Test
    void whenCallFindAll_thenReturnsList() throws Exception {
        List<Stamp> stamps = List.of(stamp);
        when(service.findAll()).thenReturn(stamps);
        mockMvc.perform(get("/api/stamp"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.stampDTOList.size()").value(stamps.size()))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].country").value("HUN"))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].yearOfIssue").value(2020))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].denominations.size()").value(0))
                .andExpect(jsonPath("$._embedded.stampDTOList[0]._links.self.href").value("http://localhost/api/stamp/1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp"));
    }

    @Test
    void whenCallFindById_thenReturnsStamp() throws Exception {
        when(service.findById(any())).thenReturn(stamp);
        mockMvc.perform(get("/api/stamp/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("butterflies"))
                .andExpect(jsonPath("$.country").value("HUN"))
                .andExpect(jsonPath("$.yearOfIssue").value(2020))
                .andExpect(jsonPath("$.denominations.size()").value(0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp/1"));
    }

    @Test
    void whenFindAllByCountry_thenReturnsList() throws Exception {
        List<Stamp> stamps = List.of(stamp);
        when(service.findAllByCountry(any())).thenReturn(stamps);
        mockMvc.perform(get("/api/stamp/country")
                .param("country", "HUN"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.stampDTOList.size()").value(stamps.size()))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].country").value("HUN"))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].yearOfIssue").value(2020))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].denominations.size()").value(0))
                .andExpect(jsonPath("$._embedded.stampDTOList[0]._links.self.href").value("http://localhost/api/stamp/1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp"));
    }

    @Test
    void whenFindAllByYear_thenReturnsList() throws Exception {
        List<Stamp> stamps = List.of(stamp);
        when(service.findAllByYear(any())).thenReturn(stamps);
        mockMvc.perform(get("/api/stamp/year")
                .param("year", String.valueOf(2020)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.stampDTOList.size()").value(stamps.size()))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].country").value("HUN"))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].yearOfIssue").value(2020))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].denominations.size()").value(0))
                .andExpect(jsonPath("$._embedded.stampDTOList[0]._links.self.href").value("http://localhost/api/stamp/1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp"));
    }

    @Test
    void whenFindAllByCountryAndYear_thenReturnsList() throws Exception {
        List<Stamp> stamps = List.of(stamp);
        when(service.findAllByCountryAndYear(any(), any())).thenReturn(stamps);
        mockMvc.perform(get("/api/stamp/countryandyear")
                .param("year", String.valueOf(2020))
                .param("country", "HUN"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.stampDTOList.size()").value(stamps.size()))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].country").value("HUN"))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].yearOfIssue").value(2020))
                .andExpect(jsonPath("$._embedded.stampDTOList[0].denominations.size()").value(0))
                .andExpect(jsonPath("$._embedded.stampDTOList[0]._links.self.href").value("http://localhost/api/stamp/1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp"));
    }

    @Test
    void whenAddNew_withValidInput_thenReturnsSameObject() throws Exception {
        when(service.findById(any())).thenReturn(stamp);
        when(service.addNew(any())).thenReturn(stamp);
        mockMvc.perform(post("/api/stamp")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"butterflies\", \"country\": \"HUN\", \"yearOfIssue\": 2020 }"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("butterflies"))
                .andExpect(jsonPath("$.country").value("HUN"))
                .andExpect(jsonPath("$.yearOfIssue").value(2020))
                .andExpect(jsonPath("$.denominations.size()").value(0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp/1"));
    }

    @Test
    void whenAddNew_withYearNotValid_thenReturnsBadRequest() throws Exception {
        when(service.findById(any())).thenReturn(stamp);
        when(service.addNew(any())).thenReturn(stamp);
        mockMvc.perform(post("/api/stamp")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"butterflies\", \"country\": \"HUN\", \"yearOfIssue\": 20 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenAddNew_withValidInput_thenTransformIntoService() throws Exception {
        when(service.findById(any())).thenReturn(stamp);
        when(service.addNew(any())).thenReturn(stamp);
        mockMvc.perform(post("/api/stamp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stampCommand)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Stamp> captor = ArgumentCaptor.forClass(Stamp.class);
        verify(service, times(1)).addNew(captor.capture());
        assertEquals("butterflies", captor.getValue().getName());
        assertNull(captor.getValue().getId());
        assertEquals(2020, captor.getValue().getYearOfIssue());
        assertEquals(0, captor.getValue().getDenominations().size());
        assertEquals("HUN", captor.getValue().getCountry());
    }

    @Test
    void whenUpdate_withValidInput_thenReturnsUpdatedStamp() throws Exception {
        when(service.findById(any())).thenReturn(stamp);
        when(service.addNew(any())).thenReturn(stamp);
        mockMvc.perform(put("/api/stamp/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"butterflies\", \"country\": \"HUN\", \"yearOfIssue\": 2020 }"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("butterflies"))
                .andExpect(jsonPath("$.country").value("HUN"))
                .andExpect(jsonPath("$.yearOfIssue").value(2020))
                .andExpect(jsonPath("$.denominations.size()").value(0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp/1"));
    }

    @Test
    void whenUpdate_withValidInput_thenTransformsIntoService() throws Exception {
        when(service.findById(any())).thenReturn(stamp);
        when(service.addNew(any())).thenReturn(stamp);
        mockMvc.perform(put("/api/stamp/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"butterflies\", \"country\": \"HUN\", \"yearOfIssue\": 2020 }"))
                .andExpect(status().isOk());

        ArgumentCaptor<Stamp> captor = ArgumentCaptor.forClass(Stamp.class);
        verify(service, times(1)).addNew(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals("butterflies", captor.getValue().getName());
        assertEquals("HUN", captor.getValue().getCountry());
        assertEquals(2020, captor.getValue().getYearOfIssue());
    }

    @Test
    void whenUpdate_withYearNotValid_thenReturnsBadRequest() throws Exception {
        when(service.findById(any())).thenReturn(stamp);
        when(service.addNew(any())).thenReturn(stamp);
        mockMvc.perform(put("/api/stamp/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"butterflies\", \"country\": \"HUN\", \"yearOfIssue\": 20 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenDelete_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/stamp/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(any());
    }

}
