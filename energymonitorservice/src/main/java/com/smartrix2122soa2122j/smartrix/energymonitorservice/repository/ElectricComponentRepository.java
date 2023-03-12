package com.smartrix2122soa2122j.smartrix.energymonitorservice.repository;

import java.util.List;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.ElectricComponent;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ElectricComponentRepository extends MongoRepository<ElectricComponent,String> {
    List<ElectricComponent> findAllByCustomerId(String customerId);
    List<ElectricComponent> findAllByCustomerName(String customerName);
}