package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity;

public class ReserveAddingEnergyRequest {

    private String region;

    private int energyRequested;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getEnergyRequested() {
        return energyRequested;
    }

    public void setEnergyRequested(int energyRequested) {
        this.energyRequested = energyRequested;
    }
}
