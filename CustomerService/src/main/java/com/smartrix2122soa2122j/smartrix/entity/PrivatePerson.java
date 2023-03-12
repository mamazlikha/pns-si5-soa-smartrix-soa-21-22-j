package com.smartrix2122soa2122j.smartrix.entity;

import javax.validation.constraints.NotBlank;

public class PrivatePerson extends Customer{

    
    @NotBlank
    private String region;
    

    public PrivatePerson(){
        this.setType("PRIVATE_PERSON");
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    
}
