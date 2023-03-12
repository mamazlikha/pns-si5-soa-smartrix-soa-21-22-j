package com.smartrix2122soa2122j.smartrix.repositories;

import java.util.Optional;

import com.smartrix2122soa2122j.smartrix.entity.BankDetails;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankDetailsRepository extends MongoRepository<BankDetails, String>{
    Optional<BankDetails>  findByCustomerId(String customerId);
}
