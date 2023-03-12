package com.smartrix2122soa2122j.smartrix.entity.dto;

import com.smartrix2122soa2122j.smartrix.entity.ElectricComponent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DTO {

    public static final record AddComponentRequest(String customerId, List<ElectricComponent> electricComponents) {

    }

    public static record ComponentSwitchRequest(String customerName, String customerId, String componentName, Boolean switchOn) {
    }

    public record MonthlyBill(String id, String customerId, LocalDate date, BigDecimal toPay, int energyUsed, String month) {

    }

    /**
     * GraphPoint String label, int energyUsed
     */
    public record GraphPoint(String label, int energyUsed) {
    }

    /**
     * OrderedConsumptionGraph String graphType, List<GraphPoint>
     * graphPoints
     */
    public static final record OrderedConsumptionGraph(String graphType, List<GraphPoint> graphPoints) {
    }

    public static final class Battery {
        private  int maximumCapacity;
        private int currentLevel;
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
}
