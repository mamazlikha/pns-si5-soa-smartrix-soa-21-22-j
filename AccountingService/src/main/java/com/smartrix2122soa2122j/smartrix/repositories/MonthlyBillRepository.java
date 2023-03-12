package com.smartrix2122soa2122j.smartrix.repositories;

import java.util.List;
import java.util.Optional;

import com.smartrix2122soa2122j.smartrix.entity.MonthlyBill;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MonthlyBillRepository extends MongoRepository<MonthlyBill,String>{
   List<MonthlyBill> findByCustomerId(String customerId);
   
   Optional<MonthlyBill> findByCustomerIdAndMonth(String customerId, String month);
}
