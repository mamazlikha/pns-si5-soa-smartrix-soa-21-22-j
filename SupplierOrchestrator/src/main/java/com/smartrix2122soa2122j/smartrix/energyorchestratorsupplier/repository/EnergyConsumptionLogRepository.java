package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository;


import java.util.Optional;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.ConsumptionEvolutionInRegion;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnergyConsumptionLogRepository extends MongoRepository<ConsumptionEvolutionInRegion, String> {

    Optional<ConsumptionEvolutionInRegion> findByRegion(String region);

    
}
