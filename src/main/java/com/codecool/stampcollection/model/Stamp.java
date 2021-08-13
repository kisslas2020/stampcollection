package com.codecool.stampcollection.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;
    private Integer yearOfIssue;
    @OneToMany(mappedBy = "stamp")
    private Set<Denomination> denominations;

    public Stamp() {
    }

    public Stamp(Long id, String name, String country, Integer yearOfIssue, Set<Denomination> denominations) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.yearOfIssue = yearOfIssue;
        this.denominations = denominations;
    }

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

    public Set<Denomination> getDenominations() {
        return denominations;
    }

    public void setDenominations(Set<Denomination> denominations) {
        this.denominations = denominations;
    }

}
