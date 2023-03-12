package com.smartrix2122soa2122j.smartrix.energymonitorservice.repository;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.DailyGlobalMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.MonthlyGlobalMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DailyGlobalMeasureRepository extends MongoRepository<DailyGlobalMeasure, String> {

    @Aggregation({ "{$match:{date: {$gte: ?0,$lt: ?1}}}",
            "{$project:{_id:0,energyUsed:1,label:$date}}" })
    List<DTO.GraphPoint> getDailyConsumptionGraphPoints(String lower, String upper);

    @Aggregation({ "{$match:{date: {$gte: ?0,$lt: ?1}}}",
            "{$project:{_id:0,energyUsed:1,label:$date}}" })
    List<DailyGlobalMeasure> findDailyGlobalMeasureRepositoryBetween(String lower, String upper);

    
}
