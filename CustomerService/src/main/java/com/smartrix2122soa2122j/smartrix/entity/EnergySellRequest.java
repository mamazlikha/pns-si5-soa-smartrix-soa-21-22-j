package com.smartrix2122soa2122j.smartrix.entity;

public class EnergySellRequest {

    private Energy energy;
    private String customerID;


    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}
