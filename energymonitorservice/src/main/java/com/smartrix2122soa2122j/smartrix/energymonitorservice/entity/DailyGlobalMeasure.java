package com.smartrix2122soa2122j.smartrix.energymonitorservice.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class DailyGlobalMeasure {

    @Id
    private String id;

    private int energyUsed;


    private String date;

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



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DailyGlobalMeasure{" +
                "energyUsed=" + energyUsed +
                ", date='" + date + '\'' +
                '}';
    }
}
