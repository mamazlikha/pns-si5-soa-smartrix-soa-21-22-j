package com.smartrixgridj.emailservice.repositories;

import java.util.List;
import java.util.Optional;

import com.smartrixgridj.emailservice.entities.Customer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer,String>{
    boolean existsByName(String name);

    Optional<Customer> findByName(String name);

    List<Customer> findByRegion(String region);
    
}
