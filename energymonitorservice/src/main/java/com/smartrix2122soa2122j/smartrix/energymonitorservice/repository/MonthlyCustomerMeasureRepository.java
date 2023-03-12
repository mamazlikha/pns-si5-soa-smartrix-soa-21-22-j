package com.smartrix2122soa2122j.smartrix.energymonitorservice.repository;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.MonthlyCustomerMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.MonthlyGlobalMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MonthlyCustomerMeasureRepository extends MongoRepository<MonthlyCustomerMeasure, String> {

    @Aggregation({ "{$match:{month:?0}}",
            "{$group:{_id:$month, energyUsed:{$sum:$energyUsed}}}",
            "{$project:{_id:0, energyUsed:1, month: $_id}}"
    })
    MonthlyGlobalMeasure aggregateMeasureOfAllCustomer(String month);

    @Aggregation({ "{$match:{date: {$gte: ?0,$lt: ?1},customerId: ?2}}",
            "{$project:{_id:0,energyUsed:1,label:$month}}" })
    List<DTO.GraphPoint> getMonthlyConsumptionGraphPoints(String lower, String upper, String customerId);
}
