package com.smartrix2122soa2122j.smartrix.energymonitorservice.repository;

import java.util.List;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.EnergyControl;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnergyControlRepository extends MongoRepository<EnergyControl,String> {

    List<EnergyControl> findByRegion(String region);
    List<EnergyControl> findByUsageSwitch(String usageSwitch);
    List<EnergyControl> findAll();

}