package com.smartrix2122soa2122j.smartrix.electricmeterregister.repository;

import java.util.Optional;

import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.Battery;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BatteryRepository extends MongoRepository<Battery, String> {

    Optional<Battery> findByCustomerId(String customerId);
}
