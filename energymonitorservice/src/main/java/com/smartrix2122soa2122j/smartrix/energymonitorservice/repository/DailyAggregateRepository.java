package com.smartrix2122soa2122j.smartrix.energymonitorservice.repository;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.DailyCustomerMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.DailyGlobalMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.MonthlyCustomerMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DailyAggregateRepository extends MongoRepository<DailyCustomerMeasure,String> {

    @Aggregation({ "{$match:{date: {$gte: ?0,$lt: ?1}}}", "{$group:{_id:$customerId, energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0, energyUsed:1, month: ?2, customerId: $_id}}"})
    List<MonthlyCustomerMeasure> aggregateMonthlyMeasureByCustomer(String lower, String upper, String month);

    @Aggregation({ "{$match:{date:?0}}", "{$group:{_id:$date, energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0, energyUsed:1, date: $_id}}" })
    DailyGlobalMeasure aggregateDailyMeasureForAllCustomers(String date);

    @Aggregation({ "{$match:{date: {$gte: ?0,$lt: ?1},customerId: ?2}}",
            "{$project:{_id:0,energyUsed:1,label:$date}}" })
    List<DTO.GraphPoint> getDailyConsumptionGraphPoints(String lower, String upper, String customerId);

    @Aggregation({ "{$match:{date: {$gte: ?0,$lt: ?1},customerId: ?2}}"})
    List<DailyCustomerMeasure> findDailyCustomerMeasure(String lower, String upper, String customerId);


    @Aggregation({"{$match:{date: {$gte: ?0,$lt: ?1},region: ?2}}",
    "{$group:{_id:$region,energyUsed:{$sum:$energyUsed}}}",
    "{$project:{_id:0, region:$_id,energyUsed:1 }}"})
    DTO.PairRegionEnergy getEnergyUsedInRegionBetween(String lower, String upper, String region);

    @Aggregation({ "{$match:{date: {$gte: ?0,$lt: ?1},customerId: ?2}}",
            "{$group:{_id:$customerId,energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0,energyUsed:1,customerId:1}}" })
    DTO.CustomerMonthlyConsumption sumCustomerConsumptionBetween(String lower, String upper, String customerId);



}
