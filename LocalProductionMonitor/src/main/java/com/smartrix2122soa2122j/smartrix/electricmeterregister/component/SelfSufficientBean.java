package com.smartrix2122soa2122j.smartrix.electricmeterregister.component;

import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.CustomerLocalProduction;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.repository.LocalProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Component
public class SelfSufficientBean {

    @Value("${energy.monitor.service.address}")
    private String energyMonitorService;

    private final RestTemplate restTemplate;

    @Autowired
    private ClockWrapper clockWrapper;

    private final Logger logger = Logger.getLogger(SelfSufficientBean.class.getName());

    @Autowired
    private LocalProductionRepository productionRepository;

    public SelfSufficientBean(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<DTO.DailyCustomerMeasure> getMeasures(String customerId, String lower, String upper){
        return Objects.requireNonNull(restTemplate.getForObject(energyMonitorService + "/measures/customer/{customerId}/from/{lower}/to/{upper}", DTO.DailyCustomerMeasureList.class, customerId, lower, upper)).measures();
    }

    public DTO.PairRegionEnergy getEnergyUsedInRegionBetween(String region, String lower, String upper){

        return restTemplate.getForObject(energyMonitorService + "/region-measures/{region}/from/{lower}/to/{upper}", DTO.PairRegionEnergy.class, region, lower, upper);

    }

    public boolean amIAutarky(String customerId) {

        logger.info("you're customer id is : " + customerId);

        logger.info("checking autarky for cutomer id :"+ customerId + ", please wait ...");


        LocalDateTime now = LocalDateTime.now(clockWrapper.getClock());

        LocalDateTime then = now.minusDays(7); // default last 7 days
        return getMeasures(customerId,then.format(DateTimeFormatter.ISO_DATE), now.format(DateTimeFormatter.ISO_DATE)).stream().mapToInt(DTO.DailyCustomerMeasure::energyUsed).sum() == 0;

    }

    public boolean isRegionAutarky(String region) {


        logger.info("checking autarky for region :"+ region + ", please wait ...");


        LocalDateTime now = LocalDateTime.now(clockWrapper.getClock());

        LocalDateTime then = now.minusDays(7); // default last 7 days
        return getEnergyUsedInRegionBetween(region, then.format(DateTimeFormatter.ISO_DATE), now.format(DateTimeFormatter.ISO_DATE)).energyUsed() == 0;

    }








}
