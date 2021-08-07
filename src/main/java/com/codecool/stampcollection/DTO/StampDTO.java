package com.codecool.stampcollection.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class StampDTO {

    private Long id;
    private String name;
    private String country;
    private Integer yearOfIssue;
    private Set<DenominationDTO> denominations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getYearOfIssue() {
        return yearOfIssue;
    }

    public void setYearOfIssue(Integer yearOfIssue) {
        this.yearOfIssue = yearOfIssue;
    }

    public Set<DenominationDTO> getDenominations() {
        return denominations;
    }

    public void setDenominations(Set<DenominationDTO> denominations) {
        this.denominations = denominations;
    }
}
