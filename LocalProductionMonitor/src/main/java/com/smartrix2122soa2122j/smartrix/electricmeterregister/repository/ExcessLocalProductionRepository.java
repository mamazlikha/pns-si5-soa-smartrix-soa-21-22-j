package com.smartrix2122soa2122j.smartrix.electricmeterregister.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.CustomerProductionSold;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.dto.DTO.MonthlyProductionSold;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExcessLocalProductionRepository extends MongoRepository<CustomerProductionSold, String> {
    

    @Aggregation({
        "{ $match: { timestamp: { $gte: ?0, $lt: ?1 } } }",
        "{ $group: { _id:$customerId, energy: { $sum: $quantity } } }",
        "{ $project: { _id: 0, customerId: $_id, energy: 1, month: ?2 } }"
    })    
    List<MonthlyProductionSold> computeProductionSold(LocalDateTime lower, LocalDateTime upper, String month);
}
