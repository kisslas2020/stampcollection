package com.codecool.stampcollection.webmvctests;

import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.DTO.TransactionCommand;
import com.codecool.stampcollection.assembler.TransactionModelAssembler;
import com.codecool.stampcollection.controller.TransactionController;
import com.codecool.stampcollection.model.Item;
import com.codecool.stampcollection.model.Transaction;
import com.codecool.stampcollection.model.TransactionType;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@Import({TransactionModelAssembler.class, MyModelMapper.class, ModelMapper.class})
public class TransactionControllerIT {

    @MockBean
    private TransactionService service;

    @MockBean
    private StampService stampService;

    @MockBean
    private DenominationService denominationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Transaction transaction;
    private TransactionCommand transactionCommand;

    @BeforeEach
    void init() {
        transaction = new Transaction(1L, LocalDate.of(2020, 3, 15), TransactionType.BUY, new ArrayList<Item>());
        transactionCommand = new TransactionCommand(LocalDate.of(2020, 3, 15), "BUY");
    }

    @Test
    void whenCallFindAll_thenReturnsList() throws Exception {
        List<Transaction> transactions = List.of(transaction);
        when(service.findAll()).thenReturn(transactions);
        mockMvc.perform(get("/api/transaction"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.transactionDTOList.size()").value(transactions.size()))
                .andExpect(jsonPath("$._embedded.transactionDTOList[0].dateOfTransaction").value("2020-03-15"))
                .andExpect(jsonPath("$._embedded.transactionDTOList[0].transactionType").value("BUY"))
                .andExpect(jsonPath("$._embedded.transactionDTOList[0]._links.self.href").value("http://localhost/api/transaction/1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/transaction"));
    }

    @Test
    void whenCallFindById_thenReturnsTransaction() throws Exception {
        when(service.findById(any())).thenReturn(transaction);
        mockMvc.perform(get("/api/transaction/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dateOfTransaction").value("2020-03-15"))
                .andExpect(jsonPath("$.transactionType").value("BUY"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/transaction/1"));
    }

    @Test
    void whenAddNew_withValidInput_thenReturnsSameObject() throws Exception {
        when(service.addNew(any())).thenReturn(transaction);
        mockMvc.perform(post("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-03-15\", \"transactionType\": \"BUY\" }"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dateOfTransaction").value("2020-03-15"))
                .andExpect(jsonPath("$.transactionType").value("BUY"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/transaction/1"));
    }

    @Test
    void whenAddNew_withNullTransactionType_thenReturnsBadRequest() throws Exception {
        when(service.addNew(any())).thenReturn(transaction);
        mockMvc.perform(post("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-03-15\", \"transactionType\": null }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenAddNew_withValidInput_thenTransformIntoService() throws Exception {
        when(service.addNew(any())).thenReturn(transaction);
        mockMvc.perform(post("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionCommand)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(service, times(1)).addNew(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals(LocalDate.of(2020, 3, 15), captor.getValue().getDateOfTransaction());
        assertEquals(TransactionType.BUY, captor.getValue().getTransactionType());
    }

    @Test
    void whenUpdate_withValidInput_thenReturnsUpdatedTransaction() throws Exception {
        when(service.findById(any())).thenReturn(transaction);
        when(service.addNew(any())).thenReturn(transaction);
        mockMvc.perform(put("/api/transaction/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-03-15\", \"transactionType\": \"BUY\" }"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dateOfTransaction").value("2020-03-15"))
                .andExpect(jsonPath("$.transactionType").value("BUY"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/transaction/1"));
    }

    @Test
    void whenUpdate_withValidInput_thenTransformsIntoService() throws Exception {
        when(service.findById(any())).thenReturn(transaction);
        when(service.addNew(any())).thenReturn(transaction);
        mockMvc.perform(put("/api/transaction/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-03-15\", \"transactionType\": \"BUY\" }"))
                .andExpect(status().isOk());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(service, times(1)).addNew(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals(LocalDate.of(2020, 3, 15), captor.getValue().getDateOfTransaction());
        assertEquals(TransactionType.BUY, captor.getValue().getTransactionType());
    }

    @Test
    void whenUpdate_withNullTransactionType_thenReturnsBadRequest() throws Exception {
        when(service.findById(any())).thenReturn(transaction);
        when(service.addNew(any())).thenReturn(transaction);
        mockMvc.perform(put("/api/transaction/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-03-15\", \"transactionType\": null }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenDelete_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/transaction/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(any());
    }

}
