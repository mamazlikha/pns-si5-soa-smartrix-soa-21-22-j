package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity;

import org.springframework.data.annotation.Id;

public class Supplier {

    @Id
    private String id;
    private String name;
    private int maximumCapacity;
    private int currentProduction;
    private String region;
    private int reserveEnergy;

    
    public int getCurrentProduction() {
        return currentProduction;
    }
    public void setCurrentProduction(int currentProduction) {
        this.currentProduction = currentProduction;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getMaximumCapacity() {
        return maximumCapacity;
    }
    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public int getReserveEnergy() {
        return reserveEnergy;
    }

    public void setReserveEnergy(int reserveEnergy) {
        this.reserveEnergy = reserveEnergy;
    }
}
