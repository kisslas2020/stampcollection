package com.codecool.stampcollection.httprequesttests;

import com.codecool.stampcollection.DTO.*;
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
public class ItemIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/item";
    }

    @Test
    void whenFindAll_withEmptyDatabase_thenReturnsEmptyList() {
        CollectionModel<EntityModel<ItemDTO>> response = restTemplate.getForObject(baseUrl, CollectionModel.class);

        assertEquals(0, response.getContent().size());
    }

    @Test
    void whenAddNew_withOnePostedItem_thenReturnsItem() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        restTemplate.postForObject("http://localhost:"  + port + "/api/stamp", stamp, StampDTO.class);
        DenominationCommand denomination = new DenominationCommand(10D, "HUF", 1L);
        restTemplate.postForObject("http://localhost:"  + port + "/api/denomination", denomination, DenominationDTO.class);
        TransactionCommand transaction = new TransactionCommand(LocalDate.of(2020, 3, 15), "BUY");
        restTemplate.postForObject("http://localhost:"  + port + "/api/transaction", transaction, TransactionDTO.class);
        ItemCommand item = new ItemCommand(1L, 5L, 10D, 1L);
        ItemDTO response = restTemplate.postForObject(baseUrl, item, ItemDTO.class);

        assertEquals(1L, response.getId());
        assertEquals(5L, response.getQuantity());
        assertEquals(10D, response.getUnitPrice());
        assertEquals(1L, response.getDenominationDTO().getId());
        assertEquals(1L, response.getTransactionDTO().getId());
    }

    @Test
    void whenUpdate_withOnePostedItem_thenReturnsUpdatedItem() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        restTemplate.postForObject("http://localhost:"  + port + "/api/stamp", stamp, StampDTO.class);
        DenominationCommand denomination = new DenominationCommand(10D, "HUF", 1L);
        restTemplate.postForObject("http://localhost:"  + port + "/api/denomination", denomination, DenominationDTO.class);
        TransactionCommand transaction = new TransactionCommand(LocalDate.of(2020, 3, 15), "BUY");
        restTemplate.postForObject("http://localhost:"  + port + "/api/transaction", transaction, TransactionDTO.class);
        ItemCommand item = new ItemCommand(1L, 5L, 10D, 1L);
        ItemDTO response = restTemplate.postForObject(baseUrl, item, ItemDTO.class);

        item.setQuantity(10L);

        restTemplate.put(baseUrl + "/1", item);
        ItemDTO updatedItem = restTemplate.getForObject(baseUrl + "/1", ItemDTO.class);
        assertEquals(10, updatedItem.getQuantity());
    }

    @Test
    void whenDelete_withOnePostedItem_thenReturnsOK() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        restTemplate.postForObject("http://localhost:"  + port + "/api/stamp", stamp, StampDTO.class);
        DenominationCommand denomination = new DenominationCommand(10D, "HUF", 1L);
        restTemplate.postForObject("http://localhost:"  + port + "/api/denomination", denomination, DenominationDTO.class);
        TransactionCommand transaction = new TransactionCommand(LocalDate.of(2020, 3, 15), "BUY");
        restTemplate.postForObject("http://localhost:"  + port + "/api/transaction", transaction, TransactionDTO.class);
        ItemCommand item = new ItemCommand(1L, 5L, 10D, 1L);
        restTemplate.postForObject(baseUrl, item, ItemDTO.class);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
