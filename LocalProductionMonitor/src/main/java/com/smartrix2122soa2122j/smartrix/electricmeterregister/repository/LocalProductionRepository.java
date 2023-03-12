package com.smartrix2122soa2122j.smartrix.electricmeterregister.repository;

import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.CustomerLocalProduction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LocalProductionRepository extends MongoRepository<CustomerLocalProduction, String> {

    List<CustomerLocalProduction> findByCustomerIdAndMomentBetween(String customerId, LocalDateTime timestamp1, LocalDateTime timestamp2);


    List<CustomerLocalProduction> findCustomerLocalProductionByRegionAndMomentBetween(String region, LocalDateTime from, LocalDateTime to);
}