package com.codecool.stampcollection.webapplicationtests;

import com.codecool.stampcollection.exception.ItemNotFoundException;
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
public class ItemWebApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    private final String baseUrl = "http://localhost:8080/api/item";

    @Test
    @Order(1)
    void whenFindAll_thenReturnsList() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(baseUrl);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.itemDTOList.size()").value(12))
                .andExpect(jsonPath("$._embedded.itemDTOList[0].denominationDTO.id").value(1L))
                .andExpect(jsonPath("$._embedded.itemDTOList[0].quantity").value(15L))
                .andExpect(jsonPath("$._embedded.itemDTOList[0].unitPrice").value(25D))
                .andExpect(jsonPath("$._embedded.itemDTOList[0].transactionDTO.id").value(1L));
    }

    @Test
    @Order(2)
    void whenFindById_withValidId_thenReturnsItem() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(baseUrl + "/{id}", 1L);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.denominationDTO.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(15L))
                .andExpect(jsonPath("$.unitPrice").value(25D))
                .andExpect(jsonPath("$.transactionDTO.id").value(1L));
    }

    @Test
    @Order(3)
    void whenFindById_withNotValidId_thenReturnsItemNotFoundException() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(baseUrl +"/{id}", 16L);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof ItemNotFoundException);
        String message = JsonPath.parse(result.getResponse().getContentAsString()).read("$.detail");
        assertTrue(message.equals("Item with id 16 not found"));
    }

    @Test
    @Order(4)
    void whenAddNew_withValidInput_thenReturnsNewItem() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": 1 }"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(13L))
                .andExpect(jsonPath("$.quantity").value(1L))
                .andExpect(jsonPath("$.unitPrice").value(1D))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost:8080/api/item/13"));
    }

    @Test
    @Order(5)
    void whenAddNew_withDenominationIdNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": null, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Denomination id must not be null"));
    }

    @Test
    @Order(6)
    void whenAddNew_withDenominationIdNotValid_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 100, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": 1 }"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Denomination with id 100 not found"));
    }

    @Test
    @Order(7)
    void whenAddNew_withQuantityNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": null, \"unitPrice\": 1, \"transactionId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Quantity must not be null"));
    }

    @Test
    @Order(8)
    void whenAddNew_withQuantityZero_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 0, \"unitPrice\": 1, \"transactionId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Quantity must be greater than 0"));
    }

    @Test
    @Order(9)
    void whenAddNew_withQuantityNegative_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": -1, \"unitPrice\": 1, \"transactionId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Quantity must be greater than 0"));
    }

    @Test
    @Order(10)
    void whenAddNew_withUnitPriceNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": null, \"transactionId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Unit price must not be null"));
    }

    @Test
    @Order(11)
    void whenAddNew_withUnitPriceZero_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": 0, \"transactionId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Unit price must be greater than 0"));
    }

    @Test
    @Order(12)
    void whenAddNew_withUnitPriceNegative_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": -1, \"transactionId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Unit price must be greater than 0"));
    }

    @Test
    @Order(13)
    void whenAddNew_withTransactionIdNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": null }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Transaction id must not be null"));
    }

    @Test
    @Order(14)
    void whenAddNew_withTransactionIdNotValid_thenReturnsNoContent() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": 100 }"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Transaction with id 100 not found"));
    }

    @Test
    @Order(15)
    void whenUpdate_withValidInput_thenReturnsUpdatedItem() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.denominationDTO.id").value(1))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.unitPrice").value(1))
                .andExpect(jsonPath("$.transactionDTO.id").value(1))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost:8080/api/item/1"));
    }

    @Test
    @Order(16)
    void whenUpdate_withDenominationIdNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": null, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Denomination id must not be null"));
    }

    @Test
    @Order(17)
    void whenUpdate_withDenominationIdNotValid_thenReturnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 100, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Denomination with id 100 not found"));
    }

    @Test
    @Order(17)
    void whenUpdate_withQuantityNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": null, \"unitPrice\": 1, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Quantity must not be null"));
    }

    @Test
    @Order(18)
    void whenUpdate_withQuantityZero_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 0, \"unitPrice\": 1, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Quantity must be greater than 0"));
    }

    @Test
    @Order(18)
    void whenUpdate_withQuantityNegative_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": -1, \"unitPrice\": 1, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Quantity must be greater than 0"));
    }

    @Test
    @Order(19)
    void whenUpdate_withUnitPriceNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": null, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Unit price must not be null"));
    }

    @Test
    @Order(19)
    void whenUpdate_withUnitPriceZero_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": 0, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Unit price must be greater than 0"));
    }

    @Test
    @Order(20)
    void whenUpdate_withUnitPricNegative_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": -1, \"transactionId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Unit price must be greater than 0"));
    }

    @Test
    @Order(20)
    void whenUpdate_withTransactionIdNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": null }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Transaction id must not be null"));
    }

    @Test
    @Order(20)
    void whenUpdate_withTransactionIdNotValid_thenReturnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"denominationId\": 1, \"quantity\": 1, \"unitPrice\": 1, \"transactionId\": 100 }");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Transaction with id 100 not found"));
    }

    @Test
    @Order(21)
    void whenDeleteById_withValidInput_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 13L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Order(22)
    void whenDeleteById_withIdLastButOne_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 11L))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Only the last item can be deleted"));
    }

    @Test
    @Order(23)
    void whenDeleteById_withIdNotValid_thenReturnsNotFound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 100L))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Item with id 100 not found"));
    }

}
