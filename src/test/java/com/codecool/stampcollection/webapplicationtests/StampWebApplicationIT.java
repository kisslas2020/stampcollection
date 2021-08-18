package com.codecool.stampcollection.webapplicationtests;

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

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class StampWebApplicationIT {

    @Autowired
    private MockMvc mockMvc;
    
    private final String baseUrl = "/api/stamp";

    @Test
    @Order(1)
    void whenFindAll_thenReturnsList() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080" + baseUrl);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.stampDTOList[4].name").value("Planets"));
    }

    @Test
    @Order(2)
    void whenFindById_withValidId_thenReturnsStamp() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080" + baseUrl + "/{id}", 5L);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Planets"))
                .andExpect(jsonPath("$.yearOfIssue").value(1980));
    }

    @Test
    @Order(3)
    void whenFindById_withNotValidId_thenReturnsStampNotFoundException() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080"+ baseUrl +"/{id}", 16L);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof StampNotFoundException);
        String message = JsonPath.parse(result.getResponse().getContentAsString()).read("$.detail");
        assertTrue(message.equals("Stamp with id 16 not found"));
    }

    @Test
    @Order(4)
    void whenAddNew_withValidInput_thenReturnsNewStamp() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Buildings of Chicago\", \"country\": \"USA\", \"yearOfIssue\": 2021 }"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(6L))
                .andExpect(jsonPath("$.name").value("Buildings of Chicago"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.yearOfIssue").value(2021))
                .andExpect(jsonPath("$.denominations.size()").value(0))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp/6"));
    }

    @Test
    @Order(5)
    void whenAddNew_withNameNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": null, \"country\": \"USA\", \"yearOfIssue\": 2021 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be blank"));
    }

    @Test
    @Order(6)
    void whenAddNew_withNameEmpty_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"\", \"country\": \"USA\", \"yearOfIssue\": 2021 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be blank"));
    }

    @Test
    @Order(7)
    void whenAddNew_withCountryNotValid_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Buildings of Chicago\", \"country\": \"US\", \"yearOfIssue\": 2021 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Use three-letter Alpha-3 code."));
    }

    @Test
    @Order(8)
    void whenAddNew_withCountryNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Buildings of Chicago\", \"country\": null, \"yearOfIssue\": 2021 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be blank"));
    }

    @Test
    @Order(9)
    void whenAddNew_withYearOfIssueGreaterThanValid_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Buildings of Chicago\", \"country\": \"USA\", \"yearOfIssue\": 2022 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Year of issue must be between 1840 and current year."));
    }

    @Test
    @Order(10)
    void whenAddNew_withYearOfIssueLessThanValid_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Buildings of Chicago\", \"country\": \"USA\", \"yearOfIssue\": 1839 }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Year of issue must be between 1840 and current year."));
    }

    @Test
    @Order(11)
    void whenAddNew_withYearOfIssueNull_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Buildings of Chicago\", \"country\": \"USA\", \"yearOfIssue\": null }"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Year of issue must be between 1840 and current year."));
    }

    @Test
    @Order(12)
    void whenUpdate_withValidInput_thenReturnsUpdatedStamp() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": \"HUN\", \"yearOfIssue\": 1966 }");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.name").value("National Holidays"))
                .andExpect(jsonPath("$.country").value("HUN"))
                .andExpect(jsonPath("$.yearOfIssue").value(1966))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp/4"));
    }

    @Test
    @Order(13)
    void whenUpdate_withNameNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": null, \"country\": \"HUN\", \"yearOfIssue\": 1966 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be blank"));
    }

    @Test
    @Order(14)
    void whenUpdate_withNameEmpty_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"\", \"country\": \"HUN\", \"yearOfIssue\": 1966 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be blank"));
    }

    @Test
    @Order(15)
    void whenUpdate_withCountryNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": null, \"yearOfIssue\": 1966 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("must not be blank"));
    }

    @Test
    @Order(16)
    void whenUpdate_withCountryNotValid_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": \"USAA\", \"yearOfIssue\": 1966 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Use three-letter Alpha-3 code."));
    }

    @Test
    @Order(17)
    void whenUpdate_withYearOfIssueNull_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": \"USA\", \"yearOfIssue\": null }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Year of issue must be between 1840 and current year."));
    }

    @Test
    @Order(18)
    void whenUpdate_withYearBottomLimit_thenReturnsUpdatedStamp() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": \"HUN\", \"yearOfIssue\": 1840 }");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.name").value("National Holidays"))
                .andExpect(jsonPath("$.country").value("HUN"))
                .andExpect(jsonPath("$.yearOfIssue").value(1840))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp/4"));
    }

    @Test
    @Order(19)
    void whenUpdate_withYearTopLimit_thenReturnsUpdatedStamp() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": \"HUN\", \"yearOfIssue\": 2021 }");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.name").value("National Holidays"))
                .andExpect(jsonPath("$.country").value("HUN"))
                .andExpect(jsonPath("$.yearOfIssue").value(2021))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/stamp/4"));
    }

    @Test
    @Order(20)
    void whenUpdate_withYearOfIssueGreater_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": \"USA\", \"yearOfIssue\": 2022 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Year of issue must be between 1840 and current year."));
    }

    @Test
    @Order(21)
    void whenUpdate_withYearOfIssueLess_thenReturnsBadRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": \"USA\", \"yearOfIssue\": 1839 }");
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Year of issue must be between 1840 and current year."));
    }

    @Test
    @Order(22)
    void whenUpdate_withIdNotValid_thenReturnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(baseUrl + "/{id}", 17L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"National Holidays\", \"country\": \"USA\", \"yearOfIssue\": 2015 }");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Stamp with id 17 not found"));
    }

    @Test
    @Order(23)
    void whenDeleteById_withValidId_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 2L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Order(24)
    void whenDeleteById_withNotValidId_thenReturnsNotFound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 16L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(25)
    void whenDeleteById_withStampWithDenomination_thenReturnsUnsupportedOperationException() throws Exception {
        mockMvc.perform(delete(baseUrl + "/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnsupportedOperationException));
    }
}
