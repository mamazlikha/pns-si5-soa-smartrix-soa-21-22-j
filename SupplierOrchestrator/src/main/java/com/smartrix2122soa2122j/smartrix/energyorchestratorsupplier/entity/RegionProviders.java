package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity;

import java.util.List;

public class RegionProviders {


    private List<Supplier> suppliers;

    private int energyReserve;

    private String region;

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public int getEnergyReserve() {
        return energyReserve;
    }

    public void setEnergyReserve(int energyReserve) {
        this.energyReserve = energyReserve;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
