package com.smartrix2122soa2122j.smartrix.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class DTO {
    /**
     * CustomerMonthlyConsumption
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CustomerMonthlyConsumption(String customerId, int energyUsed) {
    }

    /**
     * CustomersMonthlyConsumptionsList List<CustomerMonthlyConsumption>
     * consumptions
     */
    public record CustomersMonthlyConsumptionsList(List<CustomerMonthlyConsumption> consumptions) {
    }

   

    public record MonthlyProductionSold(String customerId, int energy, String month){

    }


    public record MonthlyProductionSoldList(List<MonthlyProductionSold> productions) {
    }

}
