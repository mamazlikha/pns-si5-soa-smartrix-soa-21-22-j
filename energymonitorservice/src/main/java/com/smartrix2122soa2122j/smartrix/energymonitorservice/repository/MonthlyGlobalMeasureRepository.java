package com.smartrix2122soa2122j.smartrix.energymonitorservice.repository;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.MonthlyGlobalMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MonthlyGlobalMeasureRepository extends MongoRepository<MonthlyGlobalMeasure, String> {

    @Aggregation({ "{$match:{month: {$gte: ?0,$lt: ?1}}}",
            "{$project:{_id:0,energyUsed:1,label:$month}}" })
    List<DTO.GraphPoint> getMonthlyConsumptionGraphPoints(String lower, String upper);
}
