package com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.DailyCustomerMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.ElectricComponent;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.Measure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.MonthlyCustomerMeasure;

import java.util.List;

public class DTO {
    /**
     * GraphPoint String label, int energyUsed
     */
    public static final record GraphPoint(String label, int energyUsed) {
    }

    public static final record PairRegionEnergy(String region, int energyUsed) {
    }

    public static final record EnergyConsumedPerRegionResponse(List<PairRegionEnergy> consumptions){
    }

    public static final record AddComponentRequest(String customerId, List<ElectricComponent> electricComponents ) {
    }

    public static final record ComponentSwitchRequest(String customerName, String customerId, String componentName, Boolean switchOn) {

    }


    public static final record MeasureList(List<Measure> measures){

    }

    public static final record DailyCustomerMeasureList(List<DailyCustomerMeasure> measures){

    }

    /**
     * CustomerMonthlyConsumption
     */
    public static final record CustomerMonthlyConsumption(String customerId, int energyUsed) {
    }

    /**
     * MonthlyCustomerConsumptionsList
     * consumptions
     */
    public record MonthlyCustomerConsumptionsList(List<MonthlyCustomerMeasure> consumptions) {
    }
}
