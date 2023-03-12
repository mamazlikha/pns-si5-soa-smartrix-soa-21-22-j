package com.smartrix2122soa2122j.smartrix.electricmeterregister.entity;

import java.time.LocalDateTime;

public class Measure {

    private LocalDateTime timestamp;

    private String customerId;

    private int energyUsed;

    private String region;

    

    public Measure(LocalDateTime timestamp, String customerId, int energyUsed, String region) {
        this.timestamp = timestamp;
        this.customerId = customerId;
        this.energyUsed = energyUsed;
        this.region = region;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getEnergyUsed() {
        return energyUsed;
    }

    public void setEnergyUsed(int energyUsed) {
        this.energyUsed = energyUsed;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }




    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "timestamp=" + timestamp +
                ", customerId='" + customerId + '\'' +
                ", energyUsed=" + energyUsed +
                ", region='" + region + '\'' +
                '}';
    }

}
