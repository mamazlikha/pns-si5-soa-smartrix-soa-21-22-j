package com.smartrix2122soa2122j.smartrix.energymonitorservice.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.DailyAggregateRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MeasureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountingInterface {

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private DailyAggregateRepository dailyAggregateRepository;

    @Autowired
    private ClockWrapper clockWrapper;

    public List<DTO.CustomerMonthlyConsumption> computeMonthlyConsumption(LocalDateTime endDateTime) {
        LocalDateTime startDateTime = LocalDateTime.of(endDateTime.getYear(), endDateTime.getMonthValue(), 1, 0, 0, 1);// le
                                                                                                                       // début
                                                                                                                       // du
                                                                                                                       // mois
        return this.measureRepository.sumCustomersConsumptionBetween(startDateTime, endDateTime);

    }

    public DTO.CustomerMonthlyConsumption computeThisMonthConsumption(String customerId) {

        var today = LocalDateTime.now(this.clockWrapper.getClock());

        LocalDateTime startDateTime = LocalDateTime.of(today.getYear(), today.getMonthValue(), 1, 0, 0, 1);// le
        // début
        // du
        // mois

        return this.dailyAggregateRepository.sumCustomerConsumptionBetween(startDateTime.format(DateTimeFormatter.ISO_DATE), today.format(DateTimeFormatter.ISO_DATE), customerId);

    }

}
