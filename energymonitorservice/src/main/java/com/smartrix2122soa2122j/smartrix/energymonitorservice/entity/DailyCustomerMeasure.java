package com.smartrix2122soa2122j.smartrix.energymonitorservice.entity;

import org.springframework.data.annotation.Id;

public class DailyCustomerMeasure {
    @Id
    private String id;

    private int energyUsed;
    private String customerId;

    private String date;

    private String region;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEnergyUsed() {
        return energyUsed;
    }

    public void setEnergyUsed(int energyUsed) {
        this.energyUsed = energyUsed;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DailyCustomerMeasure{" +
                "energyUsed=" + energyUsed +
                ", customerId='" + customerId + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
