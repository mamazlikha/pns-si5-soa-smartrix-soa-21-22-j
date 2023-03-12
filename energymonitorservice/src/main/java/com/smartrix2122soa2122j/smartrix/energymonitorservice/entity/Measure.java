package com.smartrix2122soa2122j.smartrix.energymonitorservice.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.annotation.Id;

public class Measure {

    @Id
    private String id;
    private LocalDateTime timestamp;

    @NotBlank
    private String customerId;

    @PositiveOrZero
    private int energyUsed;

    @NotBlank
    private String region;
    private String usageType;

    private String componentId;

    public Measure() {
    }

    public Measure(LocalDateTime timestamp, String customerId, int energyUsed, String region) {
        this.timestamp = timestamp;
        this.customerId = customerId;
        this.energyUsed = energyUsed;
        this.region = region;
    }

    public Measure(LocalDateTime timestamp, String customerId, int energyUsed, String region, String componentId) {
        this.timestamp = timestamp;
        this.customerId = customerId;
        this.energyUsed = energyUsed;
        this.region = region;
        this.componentId = componentId;
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public int getEnergyUsed() {
        return energyUsed;
    }
    public void setEnergyUsed(int energyUsed) {
        this.energyUsed = energyUsed;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }
    
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", customerId='" + customerId + '\'' +
                ", energyUsed=" + energyUsed +
                ", region='" + region + '\'' +
                ", usageType='" + usageType + '\'' +
                ", componentId='" + componentId + '\'' +
                '}';
    }


}
