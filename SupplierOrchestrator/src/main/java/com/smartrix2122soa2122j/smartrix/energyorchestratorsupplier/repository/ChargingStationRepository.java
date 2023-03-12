package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.ChargingStation;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChargingStationRepository extends MongoRepository<ChargingStation,String>{
    
}
