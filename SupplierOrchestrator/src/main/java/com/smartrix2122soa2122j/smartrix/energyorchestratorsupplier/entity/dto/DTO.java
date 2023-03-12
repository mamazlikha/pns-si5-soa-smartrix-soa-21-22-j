package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.dto;

import java.util.List;

public class DTO {
    public static final record PairRegionEnergy(String region, int energyUsed) {
    }

    public static final record EnergyConsumedPerRegionResponse(List<PairRegionEnergy> consumptions){
    }
}
