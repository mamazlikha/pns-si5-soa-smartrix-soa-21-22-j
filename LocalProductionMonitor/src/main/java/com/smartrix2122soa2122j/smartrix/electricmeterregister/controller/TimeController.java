package com.smartrix2122soa2122j.smartrix.electricmeterregister.controller;

import com.smartrix2122soa2122j.smartrix.electricmeterregister.component.ClockWrapper;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.component.MonthlyExcessProductionAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@RestController
public class TimeController {

    @Autowired
    private ClockWrapper wrapper;

    @Autowired
    private MonthlyExcessProductionAggregator aggregator;

    @GetMapping("/admin/time/{clockReference}")
    public void changeClock(@PathVariable("clockReference") String clockStr) {
        this.wrapper.setClock(Clock.fixed(Instant.parse(clockStr), ZoneOffset.ofHours(0)));

    }


    @GetMapping("/admin/monthly-scheduler")
    public void computeCustomersProduction() {
        this.aggregator.aggregateExcessProduction();
    }

}
