package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;

public class ConsumptionEvolutionInRegion {

    @Id
    private String id;

    @NotBlank
    private String region;
    private int energyT1;
    private int energyT2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEnergyT1() {
        return energyT1;
    }

    public void setEnergyT1(int energyT1) {
        this.energyT1 = energyT1;
    }

    public int getEnergyT2() {
        return energyT2;
    }

    public void setEnergyT2(int energyT2) {
        this.energyT2 = energyT2;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "ConsumptionEvolutionInRegion [energyT1=" + energyT1 + ", energyT2=" + energyT2 + ", id=" + id
                + ", region=" + region + "]";
    }



}
