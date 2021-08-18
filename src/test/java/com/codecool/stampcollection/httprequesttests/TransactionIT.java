package com.codecool.stampcollection.httprequesttests;

import com.codecool.stampcollection.DTO.TransactionCommand;
import com.codecool.stampcollection.DTO.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("http")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/transaction";
    }

    @Test
    void whenFindAll_withEmptyDatabase_thenReturnsEmptyList() {
        CollectionModel<EntityModel<TransactionDTO>> response = restTemplate.getForObject(baseUrl, CollectionModel.class);

        assertEquals(0, response.getContent().size());
    }

    @Test
    void whenAddNew_withOnePostedTransaction_thenReturnsTransaction() {
        TransactionCommand transaction = new TransactionCommand(LocalDate.of(2020, 3, 15), "BUY");
        TransactionDTO response = restTemplate.postForObject(baseUrl, transaction, TransactionDTO.class);

        assertEquals(1L, response.getId());
        assertEquals("BUY", response.getTransactionType().getName());
        assertEquals("2020-03-15", response.getDateOfTransaction().toString());
        assertEquals(0, response.getItemDTOList().size());
    }

    @Test
    void whenUpdate_withOnePostedTransaction_thenReturnsUpdatedTransaction() {
        TransactionCommand transaction = new TransactionCommand(LocalDate.of(2020, 3, 15), "BUY");
        restTemplate.postForObject(baseUrl, transaction, TransactionDTO.class);

        transaction.setDateOfTransaction(LocalDate.of(2020, 4, 20));

        restTemplate.put(baseUrl + "/1", transaction);
        TransactionDTO updatedTransaction = restTemplate.getForObject(baseUrl + "/1", TransactionDTO.class);
        assertEquals("2020-04-20", updatedTransaction.getDateOfTransaction().toString());
    }

    @Test
    void whenDelete_withOnePostedTransaction_thenReturnsOK() {
        TransactionCommand transaction = new TransactionCommand(LocalDate.of(2020, 3, 15), "BUY");
        restTemplate.postForObject(baseUrl, transaction, TransactionDTO.class);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
