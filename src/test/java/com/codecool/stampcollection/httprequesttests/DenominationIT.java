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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("http")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DenominationIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/denomination";
    }

    @Test
    void whenFindAll_withEmptyDatabase_thenReturnsEmptyList() {
        CollectionModel<EntityModel<DenominationDTO>> response = restTemplate.getForObject(baseUrl, CollectionModel.class);

        assertEquals(0, response.getContent().size());
    }

    @Test
    void whenAddNew_withOnePostedDenomination_thenReturnsDenomination() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        StampDTO stampDTO = restTemplate.postForObject("http://localhost:"  + port + "/api/stamp", stamp, StampDTO.class);
        DenominationCommand denomination = new DenominationCommand(10D, "HUF", 1L);
        DenominationDTO response = restTemplate.postForObject(baseUrl, denomination, DenominationDTO.class);

        assertEquals(1L, response.getId());
        assertEquals(10D, response.getValue());
        assertEquals("HUF", response.getCurrency().getCurrencyCode());
        assertEquals(0, response.getStock());
        assertEquals(1L, response.getStampId());
    }

    @Test
    void whenUpdate_withOnePostedDenomination_thenReturnsUpdatedDenomination() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        StampDTO stampDTO = restTemplate.postForObject("http://localhost:"  + port + "/api/stamp", stamp, StampDTO.class);
        DenominationCommand denomination = new DenominationCommand(10D, "HUF", 1L);
        DenominationDTO response = restTemplate.postForObject(baseUrl, denomination, DenominationDTO.class);

        denomination.setValue(20D);

        restTemplate.put(baseUrl + "/1", denomination);
        DenominationDTO updatedDenomination = restTemplate.getForObject(baseUrl + "/1", DenominationDTO.class);
        assertEquals(20, updatedDenomination.getValue());
    }

    @Test
    void whenDelete_withOnePostedDenomination_thenReturnsOK() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        StampDTO stampDTO = restTemplate.postForObject("http://localhost:"  + port + "/api/stamp", stamp, StampDTO.class);
        DenominationCommand denomination = new DenominationCommand(10D, "HUF", 1L);
        restTemplate.postForObject(baseUrl, denomination, DenominationDTO.class);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
