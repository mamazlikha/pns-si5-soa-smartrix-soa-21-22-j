package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.services;

import java.util.Optional;
import java.util.logging.Logger;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.CarChargingRequest;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.ChargingStation;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.Supplier;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.ChargingStationRepository;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.EnergyConsumptionLogRepository;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarChargingWS implements ICarChargingWS {

    @Autowired
    private ChargingStationRepository chargingStationRepository;
    @Autowired
    private EnergyConsumptionLogRepository energyConsumptionLog;

    @Autowired
    private SupplierRepository supplierRepository;

    private final Logger logger = Logger.getLogger(CarChargingWS.class.getName());

    @Override
    public boolean plugCar(CarChargingRequest carEnergyRequest) {

        Optional<ChargingStation> cS = this.chargingStationRepository.findById(carEnergyRequest.getChargingStationID());
        if (cS.isEmpty()) {
            logger.info("Charging station unknown.");
            return false;
        }

        ChargingStation chargingStation = cS.get();
        var suppliers = this.supplierRepository.findByRegion(chargingStation.getRegion());

        var consumptionPerRegionLatestOpt = this.energyConsumptionLog.findByRegion(chargingStation.getRegion());

        if (suppliers.isEmpty()) {
            logger.info("Pas de centrale électrique dans cette region " + chargingStation.getRegion());
            return false;
        }
        if (consumptionPerRegionLatestOpt.isEmpty()) {
            logger.info("Pas d'information de consommation dans cette région " + chargingStation.getRegion());
            return false;
        }
        int maximumCapacity = suppliers.stream().mapToInt(Supplier::getMaximumCapacity).sum();

        if (consumptionPerRegionLatestOpt.get().getEnergyT2() == -1) {
            return (consumptionPerRegionLatestOpt.get().getEnergyT1() < 0.8 * maximumCapacity);
        }
        return (consumptionPerRegionLatestOpt.get().getEnergyT2() < 0.8 * maximumCapacity);

    }
}
