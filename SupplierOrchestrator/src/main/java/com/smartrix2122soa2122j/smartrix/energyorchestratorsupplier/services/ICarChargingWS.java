package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.services;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.CarChargingRequest;

public interface ICarChargingWS {
    boolean plugCar(CarChargingRequest carEnergyRequest);
}
