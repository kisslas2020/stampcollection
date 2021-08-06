package com.codecool.stampcollection.DTO;


public class ItemDTO {

    private Long id;
    private DenominationDTO denominationDTO;
    private Long quantity;
    private Double unitPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DenominationDTO getDenominationDTO() {
        return denominationDTO;
    }

    public void setDenominationDTO(DenominationDTO denominationDTO) {
        this.denominationDTO = denominationDTO;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
