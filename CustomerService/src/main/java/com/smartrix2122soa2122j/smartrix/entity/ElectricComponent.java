package com.smartrix2122soa2122j.smartrix.entity;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class ElectricComponent {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String customerId;

    @NotBlank
    private String customerName;

    @NotBlank
    private String region;

    @PositiveOrZero
    private int energyUsed;

    @NotBlank
    private String usageType;

    @NotBlank
    private Boolean isOn;

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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getEnergyUsed() {
        return energyUsed;
    }

    public void setEnergyUsed(int energyUsed) {
        this.energyUsed = energyUsed;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public Boolean getIsOn() {
        return isOn;
    }

    public void setIsOn(Boolean isOn) {
        this.isOn = isOn;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "ElectricComponent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", region='" + region + '\'' +
                ", energyUsed=" + energyUsed +
                ", usageType='" + usageType + '\'' +
                ", isOn=" + isOn +
                '}';
    }
}
