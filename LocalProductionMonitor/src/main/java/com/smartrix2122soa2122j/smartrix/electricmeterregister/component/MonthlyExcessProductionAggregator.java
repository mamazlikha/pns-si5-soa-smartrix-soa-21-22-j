package com.smartrix2122soa2122j.smartrix.electricmeterregister.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.dto.DTO.MonthlyProductionSoldList;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.repository.ExcessLocalProductionRepository;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.repository.LocalProductionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Scope
public class MonthlyExcessProductionAggregator {

    @Autowired
    private ExcessLocalProductionRepository excessLocalProductionRepository;
    @Autowired
    private LocalProductionRepository productionRepository;

    @Autowired
    private ClockWrapper clockWrapper;
    private final RestTemplate restTemplate;

    @Value("${accounting.service.url}")
    private String accountingUrl;

    @Value("${scheduler.activated}")
    private boolean schedulingActive;

    public MonthlyExcessProductionAggregator(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Scheduled(cron = "* * * 2 * *")
    public void scheduledAggregateExcessProduction() {
        if(schedulingActive){
            this.aggregateExcessProduction();
        }
        
    }

    
    public void aggregateExcessProduction() {

        var today = LocalDateTime.now(clockWrapper.getClock());

        var allProductions = this.excessLocalProductionRepository.computeProductionSold(
                today.minusMonths(1).withDayOfMonth(1), today.withDayOfMonth(1),
                today.format(DateTimeFormatter.ofPattern("u-MM")));

        this.restTemplate.postForEntity(accountingUrl + "/bills/customer/energy/sale",
                new MonthlyProductionSoldList(allProductions), String.class);

    }
}
