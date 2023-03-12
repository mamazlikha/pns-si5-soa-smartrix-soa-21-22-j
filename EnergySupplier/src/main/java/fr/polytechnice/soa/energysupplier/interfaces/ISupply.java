package fr.polytechnice.soa.energysupplier.interfaces;

import fr.polytechnice.soa.energysupplier.models.AdjustmentRequest;
import fr.polytechnice.soa.energysupplier.models.EnergySuppliedRequest;

public interface ISupply {

    void adjustEnergySupplied(AdjustmentRequest request);
    void getEnergyCurrentlySupplied(EnergySuppliedRequest energySuppliedRequest);
    void getProductionCapacity(EnergySuppliedRequest energySuppliedRequest);
}
