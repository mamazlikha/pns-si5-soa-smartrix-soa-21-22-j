package com.smartrix2122soa2122j.smartrix.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;

public class MonthlyBill {
    @Id
    private String id;
    @NotBlank
    private String customerId;

    private LocalDate date;

    private String month;

    private BigDecimal toPay;
    private int energyUsed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getToPay() {
        return toPay;
    }

    public void setToPay(BigDecimal toPay) {
        this.toPay = toPay;
    }

    public int getEnergyUsed() {
        return energyUsed;
    }

    public void setEnergyUsed(int energyUsed) {
        this.energyUsed = energyUsed;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "MonthlyBill{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", date=" + date +
                ", month='" + month + '\'' +
                ", toPay=" + toPay +
                ", energyUsed=" + energyUsed +
                '}';
    }
}
