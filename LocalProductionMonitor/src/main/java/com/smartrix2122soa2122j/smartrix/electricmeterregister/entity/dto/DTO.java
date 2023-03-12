package com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class DTO {

    public static final record DailyCustomerMeasureList(List<DailyCustomerMeasure> measures){

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final record DailyCustomerMeasure(int energyUsed, String customerId, String date){

    }

    public static final record DailyGlobalMeasureList(List<DailyGlobalMeasure> measures){

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final record DailyGlobalMeasure(int energyUsed, String date){

    }

    public static final record PairRegionEnergy(String region, int energyUsed) {
    }

    public record Production(int energy) {

    }

    public record MonthlyProductionSold(String customerId, int energy, String month){

    }


    public record MonthlyProductionSoldList(List<MonthlyProductionSold> productions) {
    }

    

    public static  record  EmailRequest(String type, String content , String destinataire) {
    }
}
