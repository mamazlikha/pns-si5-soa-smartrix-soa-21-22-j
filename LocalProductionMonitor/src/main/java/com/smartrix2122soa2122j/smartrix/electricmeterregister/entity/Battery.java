package com.smartrix2122soa2122j.smartrix.electricmeterregister.entity;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

public class Battery {

    @Id
    private String id;
    @Positive
    private  int maximumCapacity;
    @PositiveOrZero
    private int currentLevel;
    @NotBlank
    private  String customerId;

    public Battery(int maximumCapacity, int currentLevel, String customerId) {
        this.maximumCapacity = maximumCapacity;
        this.currentLevel = currentLevel;
        this.customerId = customerId;
    }



    public int getMaximumCapacity() {
        return maximumCapacity;
    }



    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }



    public int getCurrentLevel() {
        return currentLevel;
    }



    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }



    public String getCustomerId() {
        return customerId;
    }



    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Battery) obj;
        return this.maximumCapacity == that.maximumCapacity &&
                this.currentLevel == that.currentLevel &&
                Objects.equals(this.customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximumCapacity, currentLevel, customerId);
    }

    @Override
    public String toString() {
        return "Battery[" +
                "maximumCapactity=" + maximumCapacity + ", " +
                "currentLevel=" + currentLevel + ", " +
                "customerId=" + customerId + ']';
    }

}
