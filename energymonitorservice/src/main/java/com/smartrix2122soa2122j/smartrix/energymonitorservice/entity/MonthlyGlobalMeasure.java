package com.smartrix2122soa2122j.smartrix.energymonitorservice.entity;

import org.springframework.data.annotation.Id;

public class MonthlyGlobalMeasure {

    @Id
    private String id;

    private int energyUsed;

    /**
     * year-month
     * Exemple : 2021-11
     */
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


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "MonthlyGlobalMeasure{" +
                "energyUsed=" + energyUsed +
                ", month='" + month + '\'' +
                '}';
    }
}
