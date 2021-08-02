package com.codecool.stampcollection.model;

import com.codecool.stampcollection.validator.YearOfIssueConstraint;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 3, max = 3, message = "Use three-letter Alpha-3 code.")
    private String country;
    @YearOfIssueConstraint(message = "Year of issue must be between 1840 and current year.")
    private Integer yearOfIssue;
    @OneToMany(mappedBy = "stamp")
    private Set<Denomination> denominations;



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
