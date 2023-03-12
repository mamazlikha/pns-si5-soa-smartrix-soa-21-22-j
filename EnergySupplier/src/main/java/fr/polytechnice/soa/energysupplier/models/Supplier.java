package fr.polytechnice.soa.energysupplier.models;

import org.springframework.data.annotation.Id;

public class Supplier {

    @Id
    private String id;
    private String name;
    private String region;
    private int maximumCapacity; // CONSTANT VALUE
    private int currentProduction; // This value is changed on demand !
    private int reserveEnergy;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCurrentProduction() {
        return currentProduction;
    }
    public void setCurrentProduction(int currentProduction) {
        this.currentProduction = currentProduction;
    }
    public int getMaximumCapacity() {
        return maximumCapacity;
    }
    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Supplier [currentProduction=" + currentProduction + ", id=" + id + ", maximumCapacity="
                + maximumCapacity + ", name=" + name + ", region=" + region + "]";
    }

    
    
    
}
