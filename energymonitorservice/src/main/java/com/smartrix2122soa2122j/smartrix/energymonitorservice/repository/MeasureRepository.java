package com.smartrix2122soa2122j.smartrix.energymonitorservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO.CustomerMonthlyConsumption;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.DailyCustomerMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.DailyGlobalMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.Measure;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeasureRepository extends MongoRepository<Measure, String> {

    List<Measure> findByTimestampBetween(LocalDateTime timestamp1, LocalDateTime timestamp2);

    List<Measure> findByTimestampBetweenAndCustomerId(LocalDateTime timestamp1, LocalDateTime timestamp2,
            String customerId);

    List<Measure> findMeasureByRegionAndTimestampBetween(String region, LocalDateTime from, LocalDateTime to);

    @Aggregation({ "{$match:{timestamp: {$gte: ?0,$lt: ?1}}}",
            "{$group:{_id:$customerId,energyUsed:{$sum:$energyUsed} }}",
            "{ $project : { _id : 0, customerId : $_id, energyUsed : 1 } }" })
    List<CustomerMonthlyConsumption> sumCustomersConsumptionBetween(LocalDateTime timestamp1, LocalDateTime timestamp2);

    @Aggregation({ "{$match:{timestamp: {$gte: ?0,$lt: ?1}, customerId: ?2 }}",
            "{$group:{_id:$customerId,energyUsed:{$sum:$energyUsed} }}",
            "{ $project : { _id : 0, customerId : $_id, energyUsed : 1 } }" })
    CustomerMonthlyConsumption sumCustomerConsumptionBetween(LocalDateTime timestamp1, LocalDateTime timestamp2,
            String customerId);

    @Aggregation({ "{$match:{timestamp: {$gte: ?0,$lt: ?1},customerId: ?2},}",
            "{$group:{_id:{$dateToString:{format:?3,date:$timestamp}},energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0,energyUsed:1,label:$_id}}" })

    List<DTO.GraphPoint> getConsumptionGraphPoints(LocalDateTime lower, LocalDateTime upper, String customerId,
            String temporality);

    @Aggregation({ "{$match:{timestamp: {$gte: ?0,$lt: ?1}}}",
            "{$group:{_id:{$dateToString:{format:?2,date:$timestamp}},energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0,energyUsed:1,label:$_id}}" })

    List<DTO.GraphPoint> getConsumptionGraphPointsForAllCustomers(LocalDateTime lower, LocalDateTime upper,
            String temporality);

    @Aggregation({ "{$match:{timestamp: {$gte: ?0,$lt: ?1}}}", "{$group:{_id:$region,energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0,energyUsed:1,region:$_id}}" })
    List<DTO.PairRegionEnergy> getConsumptionsGroupedByRegion(LocalDateTime lower, LocalDateTime upper);

    @Aggregation({ "{$match:{timestamp: {$gte: ?0,$lt: ?1}}}", "{$group:{_id:$customerId, energyUsed:{$sum:$energyUsed}, region: {$last:$region} }}",//
            "{$project:{_id:0, energyUsed:1, date: ?2, customerId: $_id, region:1}}" })

    List<DailyCustomerMeasure> aggregateDailyMeasureByCustomer(LocalDateTime lower, LocalDateTime upper, String date);

    @Aggregation({ "{$match:{timestamp: {$gte: ?0,$lt: ?1}}}", "{$group:{_id:{$dateToString:{format:%u-%M-%d,date:$timestamp}}, energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0, energyUsed:1, date: $_id}}" })
    List<DailyGlobalMeasure> aggregateDailyMeasureForAllCustomers(LocalDateTime lower, LocalDateTime upper);
    @Aggregation({ "{$match:{timestamp: {$gte: ?0,$lt: ?1},region:?2}}", "{$group:{_id:$region,energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0,energyUsed:1,region:$_id}}" })
    DTO.PairRegionEnergy getConsumptionsOfRegion(LocalDateTime lower, LocalDateTime upper, String region);


}