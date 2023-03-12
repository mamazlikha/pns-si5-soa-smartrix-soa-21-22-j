package com.smartrix2122soa2122j.smartrix.energymonitorservice.controller;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.MeasureAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasureAggregatorController {

    @Autowired
    private MeasureAggregator measureAggregator;

    @GetMapping("/daily-scheduler")
    public void dailyAggregate(){
        this.measureAggregator.dailyAggregate();
    }

    @GetMapping("/monthly-scheduler")
    public void monthlyAggregate(){
        this.measureAggregator.monthlyAggregate();
    }


}
