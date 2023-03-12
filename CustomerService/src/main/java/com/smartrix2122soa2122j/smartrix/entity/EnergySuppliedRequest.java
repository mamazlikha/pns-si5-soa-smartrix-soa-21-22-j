package com.smartrix2122soa2122j.smartrix.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class EnergySuppliedRequest {

    private String name;

    public String getName() {
        return name;
    }


    public void setSupplierName(String name) {
        this.name = name;
    }

}

