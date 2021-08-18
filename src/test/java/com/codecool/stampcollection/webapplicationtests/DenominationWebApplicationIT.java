package com.codecool.stampcollection.webapplicationtests;

import com.codecool.stampcollection.exception.DenominationNotFoundException;
import com.codecool.stampcollection.exception.StampNotFoundException;
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
public class DenominationWebApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    private final String baseUrl = "/api/denomination";

    @Test
    @Order(1)
    void whenFindAll_thenReturnsList() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080" + baseUrl);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.denominationDTOList.size()").value(11))
                .andExpect(jsonPath("$._embedded.denominationDTOList[0].stock").value(26L))
                .andExpect(jsonPath("$._embedded.denominationDTOList[0].value").value(10D));
    }

    @Test
    @Order(2)
    void whenFindById_withValidId_thenReturnsDenomination() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080" + baseUrl + "/{id}", 1L);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.currency").value("HUF"))
                .andExpect(jsonPath("$.stock").value(26L))
                .andExpect(jsonPath("$.value").value(10D))
                .andExpect(jsonPath("$.stampId").value(1L));
    }

    @Test
    @Order(3)
    void whenFindById_withNotValidId_thenReturnsDenominationNotFoundException() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080" + baseUrl +"/{id}", 16L);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof DenominationNotFoundException);
        String message = JsonPath.parse(result.getResponse().getContentAsString()).read("$.detail");
        assertTrue(message.equals("Denomination with id 16 not found"));
    }

    @Test
    @Order(4)
    void whenAddNew_withValidInput_thenReturnsNewDenomination() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": 23, \"stampId\": 1 }"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(12L))
                .andExpect(jsonPath("$.currency").value("HUF"))
                .andExpect(jsonPath("$.value").value(23))
                .andExpect(jsonPath("$.stampId").value(1))
                .andExpect(jsonPath("$.stock").value(0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/denomination/12"));
    }

    @Test
    @Order(5)
    void whenAddNew_withCurrencyNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": null, \"value\": 23, \"stampId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Use three-letter currency codes"));
    }

    @Test
    @Order(6)
    void whenAddNew_withCurrencyNotValid_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"AAA\", \"value\": 23, \"stampId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Currency does not exists with code: AAA"));
    }

    @Test
    @Order(7)
    void whenAddNew_withValueNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": null, \"stampId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be null"));
    }

    @Test
    @Order(8)
    void whenAddNew_withValueNegative_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": -10, \"stampId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must be greater than 0"));
    }

    @Test
    @Order(9)
    void whenAddNew_withValueZero_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": 0, \"stampId\": 1 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must be greater than 0"));
    }

    @Test
    @Order(10)
    void whenAddNew_withStampIdNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": 10, \"stampId\": null }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be null"));
    }

    @Test
    @Order(11)
    void whenAddNew_withStampIdNotValid_thenReturnsNotFound() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": 10, \"stampId\": 16 }"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Stamp with id 16 not found"));
    }

    @Test
    @Order(12)
    void whenUpdate_withValidInput_thenReturnsUpdatedDenomination() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": 13, \"stampId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.stock").value(26L))
                .andExpect(jsonPath("$.value").value(13D))
                .andExpect(jsonPath("$.stampId").value(1L))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/denomination/1"));
    }

    @Test
    @Order(13)
    void whenUpdate_withCurrencyNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": null, \"value\": 13, \"stampId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Use three-letter currency codes"));
    }

    @Test
    @Order(14)
    void whenUpdate_withCurrencyNotValid_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"AAA\", \"value\": 13, \"stampId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Currency does not exists with code: AAA"));
    }

    @Test
    @Order(15)
    void whenUpdate_withValueNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": null, \"stampId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be null"));
    }

    @Test
    @Order(16)
    void whenUpdate_withValueNegative_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": -10, \"stampId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must be greater than 0"));
    }

    @Test
    @Order(17)
    void whenUpdate_withValueZero_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": 0, \"stampId\": 1 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must be greater than 0"));
    }

    @Test
    @Order(18)
    void whenUpdate_withStampIdNotValid_thenReturnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"currency\": \"HUF\", \"value\": 13, \"stampId\": 16 }");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Stamp with id 16 not found"));
    }

    @Test
    @Order(19)
    void whenDeleteById_withValidIdAndNoStock_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 6L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Order(20)
    void whenDeleteById_withValidIdAndNoStock_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnsupportedOperationException))
                .andExpect(jsonPath("$.detail").value("Cannot delete denomination with activ stock value"));
    }

    @Test
    @Order(21)
    void whenDeleteById_withIdNotValid_thenReturnsNotFound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 16L))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DenominationNotFoundException))
                .andExpect(jsonPath("$.detail").value("Denomination with id 16 not found"));
    }

}
