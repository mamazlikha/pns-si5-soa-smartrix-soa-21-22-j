package com.smartrix2122soa2122j.smartrix.electricmeterregister.entity;

public class CustomerProductionConsumption {


    private CustomerLocalProduction production;

    private Measure consumption;


    public CustomerLocalProduction getProduction() {
        return production;
    }

    public void setProduction(CustomerLocalProduction production) {
        this.production = production;
    }

    public Measure getConsumption() {
        return consumption;
    }

    public void setConsumption(Measure consumption) {
        this.consumption = consumption;
    }


    @Override
    public String toString() {
        return "CustomerProductionConsumption{" +
                "production=" + production +
                ", consumption=" + consumption +
                '}';
    }
}
