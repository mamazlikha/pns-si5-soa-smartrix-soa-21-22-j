package com.smartrix2122soa2122j.smartrix.energymonitorservice.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.ClockWrapper;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.DailyAggregateRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MeasureRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO.GraphPoint;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MonthlyCustomerMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerInterface {

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private DailyAggregateRepository dailyAggregateRepository;

    @Autowired
    private MonthlyCustomerMeasureRepository monthlyCustomerMeasureRepository;

    @Autowired
    private ClockWrapper clockWrapper;

    private final Logger logger = Logger.getLogger("[MonitorService] " + CustomerInterface.class.getName());

    @GetMapping("/consumptions/{customerId}/{graphType}")
    public ResponseEntity<OrderedConsumptionGraph> computeConsumationForConsumer(@PathVariable("customerId") String customerId, @PathVariable("graphType") String graphType) {
        logger.info(customerId + " " + graphType);
            switch (graphType) {
            case "LAST_DAY_BY_HOUR": {
                String format="%H %u";
                LocalDateTime lower;
                LocalDateTime upper;
                lower=LocalDateTime.now(clockWrapper.getClock()).minusDays(1);
                upper= LocalDateTime.now(clockWrapper.getClock());
                return ResponseEntity.ok(new OrderedConsumptionGraph(graphType,this.measureRepository.getConsumptionGraphPoints( lower,upper , customerId, format) ));
            }

            case "LAST_MONTH_BY_DAY": {

                var lower=LocalDateTime.now(clockWrapper.getClock()).minusMonths(1).format(DateTimeFormatter.ISO_DATE);
                var upper= LocalDateTime.now(clockWrapper.getClock()).format(DateTimeFormatter.ISO_DATE);
                return ResponseEntity.ok(new OrderedConsumptionGraph(graphType,this.dailyAggregateRepository.getDailyConsumptionGraphPoints( lower,upper , customerId) ));
            }

            case "LAST_YEAR_BY_MONTH": {

                var lower=LocalDateTime.now(clockWrapper.getClock()).minusYears(1).format(DateTimeFormatter.ofPattern("u-MM"));
                var upper= LocalDateTime.now(clockWrapper.getClock()).format(DateTimeFormatter.ofPattern("u-MM"));

                return ResponseEntity.ok(new OrderedConsumptionGraph(graphType,this.monthlyCustomerMeasureRepository.getMonthlyConsumptionGraphPoints( lower,upper , customerId) ));

            }


            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }





    }



    /**
     * OrderedConsumptionGraph String graphType, List<GraphPoint>
     * graphPoints
     */
    public record OrderedConsumptionGraph(String graphType, List<GraphPoint> graphPoints) {
    }

}
