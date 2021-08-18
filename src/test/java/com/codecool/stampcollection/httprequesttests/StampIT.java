package com.codecool.stampcollection.httprequesttests;

import com.codecool.stampcollection.DTO.StampCommand;
import com.codecool.stampcollection.DTO.StampDTO;
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
public class StampIT {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/stamp";
    }

    @Test
    void whenFindAll_withEmptyDatabase_thenReturnsEmptyList() {
        CollectionModel<EntityModel<StampDTO>> response = restTemplate.getForObject(baseUrl, CollectionModel.class);

        assertEquals(0, response.getContent().size());
    }

    @Test
    void whenAddNew_withOnePostedStamp_thenReturnsStamp() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        StampDTO response = restTemplate.postForObject(baseUrl, stamp, StampDTO.class);

        assertEquals(1L, response.getId());
        assertEquals("Butterflies", response.getName());
        assertEquals(2020, response.getYearOfIssue());
        assertEquals("HUN", response.getCountry());
        assertEquals(0, response.getDenominations().size());
    }

    @Test
    void whenUpdate_withOnePostedStamp_thenReturnsUpdatedStamp() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        restTemplate.postForObject(baseUrl, stamp, StampDTO.class);

        stamp.setCountry("USA");

        restTemplate.put(baseUrl + "/1", stamp);
        StampDTO updatedStamp = restTemplate.getForObject(baseUrl + "/1", StampDTO.class);
        assertEquals("USA", updatedStamp.getCountry());
    }

    @Test
    void whenDelete_withOnePostedStamp_thenReturnsOK() {
        StampCommand stamp = new StampCommand("Butterflies", "HUN", 2020);
        restTemplate.postForObject(baseUrl, stamp, StampDTO.class);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
