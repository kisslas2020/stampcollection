package com.codecool.stampcollection.webmvctests;

import com.codecool.stampcollection.DTO.ItemCommand;
import com.codecool.stampcollection.DTO.MyModelMapper;
import com.codecool.stampcollection.assembler.ItemModelAssembler;
import com.codecool.stampcollection.controller.ItemController;
import com.codecool.stampcollection.model.*;
import com.codecool.stampcollection.service.DenominationService;
import com.codecool.stampcollection.service.ItemService;
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

@WebMvcTest(ItemController.class)
@Import({ItemModelAssembler.class, MyModelMapper.class, ModelMapper.class})
public class ItemControllerIT {

    @MockBean
    private ItemService service;

    @MockBean
    private DenominationService denominationService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private StampService stampService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Item item;
    private ItemCommand itemCommand;
    private Stamp stamp;
    private Denomination denomination;
    private Transaction transaction;

    @BeforeEach
    void init() {
        stamp = new Stamp(1L, "butterflies", "HUN", 2020, new HashSet<Denomination>());
        denomination = new Denomination(1L, 10.0, Currency.getInstance("HUF"), stamp, 15L);
        transaction = new Transaction(1L, LocalDate.of(2020, 3, 15), TransactionType.BUY, new ArrayList<Item>());
        item = new Item(1L, denomination, 20L, 10.0, transaction);
        itemCommand = new ItemCommand(1L, 20L, 10.0, 1L);
    }

    @Test
    void whenCallFindAll_thenReturnsList() throws Exception {
        List<Item> items = List.of(item);
        when(service.findAll()).thenReturn(items);
        mockMvc.perform(get("/api/item"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.itemDTOList.size()").value(items.size()))
                .andExpect(jsonPath("$._embedded.itemDTOList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.itemDTOList[0].quantity").value(20L))
                .andExpect(jsonPath("$._embedded.itemDTOList[0].unitPrice").value(10.0))
                .andExpect(jsonPath("$._embedded.itemDTOList[0]._links.self.href").value("http://localhost/api/item/1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/item"));
    }

    @Test
    void whenCallFindById_thenReturnsItem() throws Exception {
        when(service.findById(any())).thenReturn(item);
        mockMvc.perform(get("/api/item/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(20L))
                .andExpect(jsonPath("$.unitPrice").value(10.0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/item/1"));
    }



    @Test
    void whenAddNew_withValidInput_thenReturnsSameObject() throws Exception {
        when(service.addNew(any())).thenReturn(item);
        mockMvc.perform(post("/api/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 20, \"unitPrice\": 10.0,  \"transactionId\": 1}"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.quantity").value(20L))
                .andExpect(jsonPath("$.unitPrice").value(10.0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/item/1"));
    }

    @Test
    void whenAddNew_withNegativeQuantity_thenReturnsBadRequest() throws Exception {
        when(service.addNew(any())).thenReturn(item);
        mockMvc.perform(post("/api/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": -20, \"unitPrice\": 10.0,  \"transactionId\": 1}"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenAddNew_withValidInput_thenTransformIntoService() throws Exception {
        when(service.addNew(any())).thenReturn(item);
        mockMvc.perform(post("/api/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemCommand)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(service, times(1)).addNew(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals(20L, captor.getValue().getQuantity());
        assertEquals(10.0, captor.getValue().getUnitPrice());
    }

    @Test
    void whenUpdate_withValidInput_thenReturnsUpdatedItem() throws Exception {
        when(service.findById(any())).thenReturn(item);
        when(denominationService.findById(any())).thenReturn(denomination);
        when(transactionService.findById(any())).thenReturn(transaction);
        when(service.addNew(any())).thenReturn(item);
        mockMvc.perform(put("/api/item/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 20, \"unitPrice\": 10.0,  \"transactionId\": 1}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(20L))
                .andExpect(jsonPath("$.unitPrice").value(10.0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/item/1"));
    }

    @Test
    void whenUpdate_withValidInput_thenTransformsIntoService() throws Exception {
        when(service.findById(any())).thenReturn(item);
        when(denominationService.findById(any())).thenReturn(denomination);
        when(transactionService.findById(any())).thenReturn(transaction);
        when(service.addNew(any())).thenReturn(item);
        mockMvc.perform(put("/api/item/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 20, \"unitPrice\": 10.0,  \"transactionId\": 1}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(service, times(1)).addNew(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals(20L, captor.getValue().getQuantity());
        assertEquals(10.0, captor.getValue().getUnitPrice());
    }

    @Test
    void whenUpdate_withNegativeUnitPrice_thenReturnsBadRequest() throws Exception {
        when(service.findById(any())).thenReturn(item);
        when(denominationService.findById(any())).thenReturn(denomination);
        when(transactionService.findById(any())).thenReturn(transaction);
        when(service.addNew(any())).thenReturn(item);
        mockMvc.perform(put("/api/item/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 20, \"unitPrice\": -10.0,  \"transactionId\": 1}"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenDelete_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/item/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(any());
    }

}
