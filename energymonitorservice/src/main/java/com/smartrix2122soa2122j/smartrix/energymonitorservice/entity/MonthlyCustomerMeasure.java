package com.smartrix2122soa2122j.smartrix.energymonitorservice.entity;

import org.springframework.data.annotation.Id;

public class MonthlyCustomerMeasure {

    @Id
    private String id;

    private int energyUsed;
    private String customerId;

    private String month;

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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "MonthlyCustomerMeasure{" +
                "energyUsed=" + energyUsed +
                ", customerId='" + customerId + '\'' +
                ", month='" + month + '\'' +
                '}';
    }
}
