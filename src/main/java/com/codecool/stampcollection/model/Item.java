package com.codecool.stampcollection.model;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Denomination denomination;
    private Long quantity;
    private Double unitPrice;
}
