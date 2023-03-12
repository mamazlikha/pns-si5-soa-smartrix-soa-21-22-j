package com.smartrix2122soa2122j.smartrix.reserveenergy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Energy {

    @Id
    private String id;
    @Positive
    private double quantity;//TODO add a capactity


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }


}
