package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.springframework.data.annotation.Id;


public class ChargingStation {

    @Id
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String region;
    @Positive
    private int slotCount;
    @Positive
    private int energyPerSlot;

    private int slotUsableCount;

    
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
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public int getSlotCount() {
        return slotCount;
    }
    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }
    public int getEnergyPerSlot() {
        return energyPerSlot;
    }
    public void setEnergyPerSlot(int energyPerSlot) {
        this.energyPerSlot = energyPerSlot;
    }
    public int getSlotUsableCount() {
        return slotUsableCount;
    }
    public void setSlotUsableCount(int slotUsableCount) {
        this.slotUsableCount = slotUsableCount;
    }

    

    
}
