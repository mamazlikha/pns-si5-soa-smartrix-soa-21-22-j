package com.smartrix2122soa2122j.smartrix.energymonitorservice.component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.DailyGlobalMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.MonthlyCustomerMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.MonthlyGlobalMeasure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.DailyAggregateRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.DailyGlobalMeasureRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MeasureRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MonthlyCustomerMeasureRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MonthlyGlobalMeasureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Scope
@Component
public class MeasureAggregator {

    @Autowired
    private MeasureRepository measureRepository;
    @Autowired
    private ClockWrapper clockWrapper;

    @Autowired
    private DailyAggregateRepository dailyAggregateRepository;

    @Autowired
    private DailyGlobalMeasureRepository dailyGlobalMeasureRepository;

    @Autowired
    private MonthlyCustomerMeasureRepository monthlyCustomerMeasureRepository;

    @Autowired
    private MonthlyGlobalMeasureRepository monthlyGlobalMeasureRepository;

    private final Logger logger = Logger.getLogger(MeasureAggregator.class.getName());

    private final RestTemplate restTemplate;

    @Value("${accounting.service.url}")
    private String accountingUrl;

    @Autowired
    public MeasureAggregator(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Scheduled(cron = "* * 1 * * *") // à 1 heure du matin
    public void dailyAggregate() {
        var lower = LocalDateTime.now(clockWrapper.getClock()).minusDays(1).withHour(0).withMinute(0).withSecond(0);

        var upper = lower.plusDays(1);

        logger.info(" " + lower + " " + upper);
        logger.info("Date " + lower);
        var results = this.measureRepository.aggregateDailyMeasureByCustomer(lower, upper,
                lower.toLocalDate().format(DateTimeFormatter.ISO_DATE));
        logger.info(" aggregateDailyMeasureByCustomer " + results.toString());
        this.dailyAggregateRepository.saveAll(results);

        DailyGlobalMeasure dailyGlobalMeasure = this.dailyAggregateRepository
                .aggregateDailyMeasureForAllCustomers(lower.toLocalDate().format(DateTimeFormatter.ISO_DATE));
        this.logger.info("aggregateDailyMeasureForAllCustomers :" + dailyGlobalMeasure);
        if (dailyGlobalMeasure != null) {
            this.dailyGlobalMeasureRepository.save(dailyGlobalMeasure);
        }

    }

    @Scheduled(cron = "* * * 2 * *") // tous les débuts de mois
    public void monthlyAggregate() {
        var today = LocalDate.now(this.clockWrapper.getClock());

        var previousMonth = today.minusMonths(1).withDayOfMonth(1);
        String lower = previousMonth.format(DateTimeFormatter.ISO_DATE);
        String upper = previousMonth.plusMonths(1).format(DateTimeFormatter.ISO_DATE);
        String month = previousMonth.format(DateTimeFormatter.ofPattern("u-MM"));

        logger.info("Lower : " + lower + " Upper " + upper + " Month " + month);
        List<MonthlyCustomerMeasure> measureList = this.dailyAggregateRepository
                .aggregateMonthlyMeasureByCustomer(lower, upper, month);

        logger.info(measureList.toString());
        this.monthlyCustomerMeasureRepository.saveAll(measureList);



        MonthlyGlobalMeasure measure = this.monthlyCustomerMeasureRepository.aggregateMeasureOfAllCustomer(month);
        if (measure != null) {
            logger.info(measure.toString());
            this.monthlyGlobalMeasureRepository.save(measure);
        }

        this.restTemplate.postForEntity(accountingUrl+"/bills/customers/monthly",new DTO.MonthlyCustomerConsumptionsList(measureList) , String.class);



    }
}
