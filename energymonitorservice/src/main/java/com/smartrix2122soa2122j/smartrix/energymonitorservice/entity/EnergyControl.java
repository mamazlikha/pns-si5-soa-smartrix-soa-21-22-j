package com.smartrix2122soa2122j.smartrix.energymonitorservice.entity;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class EnergyControl {

    @Id
    private String id;

    @NotBlank
    private String region;

    private LocalDateTime time;

    @NotBlank
    private String usageSwitch;

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getUsageSwitch() {
        return usageSwitch;
    }

    public void setUsageSwitch(String usageSwitch) {
        this.usageSwitch = usageSwitch;
    }

    @Override
    public String toString() {
        return "EnergyControl{" +
                "id='" + id + '\'' +
                ", region='" + region + '\'' +
                ", time=" + time +
                ", usageSwitch=" + usageSwitch +
                '}';
    }
}
