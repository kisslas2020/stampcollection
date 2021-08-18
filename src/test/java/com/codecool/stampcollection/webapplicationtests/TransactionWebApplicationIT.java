package com.codecool.stampcollection.webapplicationtests;

import com.codecool.stampcollection.exception.TransactionNotFoundException;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class TransactionWebApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    private final String baseUrl = "/api/transaction";

    @Test
    @Order(1)
    void whenFindAll_thenReturnsList() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080" + baseUrl);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.transactionDTOList.size()").value(3))
                .andExpect(jsonPath("$._embedded.transactionDTOList[0].dateOfTransaction").value("2021-03-15"))
                .andExpect(jsonPath("$._embedded.transactionDTOList[0].transactionType").value("BUY"));
    }

    @Test
    @Order(2)
    void whenFindById_withValidId_thenReturnsTransaction() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080" + baseUrl + "/{id}", 1L);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.dateOfTransaction").value("2021-03-15"))
                .andExpect(jsonPath("$.transactionType").value("BUY"));
    }

    @Test
    @Order(3)
    void whenFindById_withNotValidId_thenReturnsTransactionNotFoundException() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080" + baseUrl +"/{id}", 16L);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof TransactionNotFoundException);
        String message = JsonPath.parse(result.getResponse().getContentAsString()).read("$.detail");
        assertTrue(message.equals("Transaction with id 16 not found"));
    }

    @Test
    @Order(4)
    void whenAddNew_withValidInput_thenReturnsNewTransaction() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"1914-01-02\", \"transactionType\": \"BUY\" }"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.dateOfTransaction").value("1914-01-02"))
                .andExpect(jsonPath("$.transactionType").value("BUY"))
                .andExpect(jsonPath("$.itemDTOList.size()").value(0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/transaction/4"));
    }

    @Test
    @Order(5)
    void whenAddNew_withDateOfTransactionNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": null, \"transactionType\": \"BUY\" }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be null"));
    }

    @Test
    @Order(6)
    void whenAddNew_withDateOfTransactionInTheFuture_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2032-01-01\", \"transactionType\": \"BUY\" }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must be a date in the past or in the present"));
    }

    @Test
    @Order(7)
    void whenAddNew_withTransactionTypeNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-02-10\", \"transactionType\": null }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Transaction type can be 'BUY' or 'SELL'"));
    }

    @Test
    @Order(8)
    void whenAddNew_withTransactionTypeNotValid_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-02-10\", \"transactionType\": \"BAD\" }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Transaction type can be 'BUY' or 'SELL'"));
    }

    @Test
    @Order(9)
    void whenUpdate_withValidInput_thenReturnsUpdatedTransaction() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-02-10\", \"transactionType\": \"BUY\" }");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dateOfTransaction").value("2020-02-10"))
                .andExpect(jsonPath("$.transactionType").value("BUY"))
                .andExpect(jsonPath("$.itemDTOList[0].denominationDTO.id").value(1))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/transaction/1"));
    }

    @Test
    @Order(10)
    void whenUpdate_withDateOfTransactionNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": null, \"transactionType\": \"BUY\" }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be null"));
    }

    @Test
    @Order(11)
    void whenUpdate_withDateOfTransactionInTheFuture_thenReturnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2032-02-10\", \"transactionType\": \"BUY\" }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must be a date in the past or in the present"));
    }

    @Test
    @Order(12)
    void whenUpdate_withTransactionTypeNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-02-10\", \"transactionType\": null }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Transaction type can be 'BUY' or 'SELL'"));
    }

    @Test
    @Order(13)
    void whenUpdate_withTransactionTypeNotValid_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"dateOfTransaction\": \"2020-02-10\", \"transactionType\": \"BAD\" }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Transaction type can be 'BUY' or 'SELL'"));
    }

    @Test
    @Order(14)
    void whenDeleteById_withValidInput_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 3L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Order(15)
    void whenDeleteById_withItems_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Cannot delete transaction that has items in it"));
    }

}
