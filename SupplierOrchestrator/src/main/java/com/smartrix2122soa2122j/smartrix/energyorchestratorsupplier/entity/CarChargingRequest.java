package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity;

public class CarChargingRequest {

    private String chargingStationID;
    private int energyQuantity;
    private int chargeDuration;

    public String getChargingStationID() {
        return chargingStationID;
    }

    public void setChargingStationID(String chargingStationID) {
        this.chargingStationID = chargingStationID;
    }

    public int getEnergyQuantity() {
        return energyQuantity;
    }

    public void setEnergyQuantity(int energyQuantity) {
        this.energyQuantity = energyQuantity;
    }

    public int getChargeDuration() {
        return chargeDuration;
    }

    public void setChargeDuration(int chargeDuration) {
        this.chargeDuration = chargeDuration;
    }

}
