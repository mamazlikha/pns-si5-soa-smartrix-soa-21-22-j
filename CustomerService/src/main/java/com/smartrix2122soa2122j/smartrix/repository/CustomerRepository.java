package com.smartrix2122soa2122j.smartrix.repository;

import java.util.Optional;

import com.smartrix2122soa2122j.smartrix.entity.Customer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer,String>{
    boolean existsByName(String name);

    Optional<Customer> findByName(String name);
    
}
