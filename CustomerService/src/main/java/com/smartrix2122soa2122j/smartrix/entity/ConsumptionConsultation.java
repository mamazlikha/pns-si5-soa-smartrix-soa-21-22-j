package com.smartrix2122soa2122j.smartrix.entity;

import java.time.LocalDateTime;

public class ConsumptionConsultation {
    

    private int energyUsed;
    private LocalDateTime from;
    private LocalDateTime to;

    public int getEnergyUsed() {
        return energyUsed;
    }
    public void setEnergyUsed(int energyUsed) {
        this.energyUsed = energyUsed;
    }
    public LocalDateTime getFrom() {
        return from;
    }
    public void setFrom(LocalDateTime from) {
        this.from = from;
    }
    public LocalDateTime getTo() {
        return to;
    }
    public void setTo(LocalDateTime to) {
        this.to = to;
    }


    @Override
    public String toString() {
        return "ConsumptionConsultation{" +
                "energyUsed=" + energyUsed +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
