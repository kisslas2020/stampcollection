package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.validator.YearOfIssueConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StampCommand {

    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 3, max = 3, message = "Use three-letter Alpha-3 code.")
    private String country;
    @YearOfIssueConstraint(message = "Year of issue must be between 1840 and current year.")
    private Integer yearOfIssue;

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

}
