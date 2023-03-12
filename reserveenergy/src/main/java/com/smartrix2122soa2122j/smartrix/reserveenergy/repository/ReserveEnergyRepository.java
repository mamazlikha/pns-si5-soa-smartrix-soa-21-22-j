package com.smartrix2122soa2122j.smartrix.reserveenergy.repository;

import com.smartrix2122soa2122j.smartrix.reserveenergy.entity.Energy;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReserveEnergyRepository extends MongoRepository<Energy, String> {



}
