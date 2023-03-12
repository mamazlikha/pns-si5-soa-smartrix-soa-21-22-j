package com.smartrix2122soa2122j.smartrix.energymonitorservice.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.ClockWrapper;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.EnergyControl;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.UsageSwitch;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.DailyGlobalMeasureRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.EnergyControlRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MeasureRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MonthlyGlobalMeasureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CEOController {
    
    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private DailyGlobalMeasureRepository dailyAggregateRepository;

    @Autowired
    private MonthlyGlobalMeasureRepository monthlyCustomerMeasureRepository;


    @Autowired
    private EnergyControlRepository energyControlRepository;

    @Autowired
    private ClockWrapper clockWrapper;

    private final Logger logger = Logger.getLogger(CEOController.class.getName());

    @GetMapping("/ceo/{graphType}")
    public ResponseEntity<CustomerInterface.OrderedConsumptionGraph> overviewConsumption(@PathVariable("graphType") String graphType){

        switch (graphType) {
            case "LAST_DAY_BY_HOUR": {
                LocalDateTime lower;
                LocalDateTime upper;
                String format;
                format="%H %u";

                lower=LocalDateTime.now(clockWrapper.getClock()).minusDays(1);
                upper= LocalDateTime.now(clockWrapper.getClock());
                return ResponseEntity.ok(new CustomerInterface.OrderedConsumptionGraph(graphType,this.measureRepository.getConsumptionGraphPointsForAllCustomers( lower,upper ,  format) ));
            }

            case "LAST_MONTH_BY_DAY": {
                var lower=LocalDateTime.now(clockWrapper.getClock()).minusMonths(1).format(DateTimeFormatter.ISO_DATE);
                var upper= LocalDateTime.now(clockWrapper.getClock()).format(DateTimeFormatter.ISO_DATE);
                return ResponseEntity.ok(new CustomerInterface.OrderedConsumptionGraph(graphType,this.dailyAggregateRepository.getDailyConsumptionGraphPoints( lower,upper ) ));
            }

            case "LAST_YEAR_BY_MONTH": {

                var lower=LocalDateTime.now(clockWrapper.getClock()).minusYears(1).format(DateTimeFormatter.ofPattern("u-MM"));
                var upper= LocalDateTime.now(clockWrapper.getClock()).format(DateTimeFormatter.ofPattern("u-MM"));
                return ResponseEntity.ok(new CustomerInterface.OrderedConsumptionGraph(graphType,this.monthlyCustomerMeasureRepository.getMonthlyConsumptionGraphPoints( lower,upper ) ));
            }


            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }



    }

    @GetMapping("/regionTotalConsumption/{region}")//TODO why this method => Used for proving US10 (before/after consumption of region)
    public int regionTotalConsumption(@PathVariable("region") String region){
        LocalDateTime lower = LocalDateTime.now(clockWrapper.getClock()).minusYears(100);
        LocalDateTime upper = LocalDateTime.now(clockWrapper.getClock()).plusYears(100);
        DTO.PairRegionEnergy result = measureRepository.getConsumptionsOfRegion(lower, upper, region);
        if(result == null)
            return 0;
        return result.energyUsed();
    }

    @PostMapping("/switchusagetype")
    public ResponseEntity<EnergyControl> switchUsagetype(@RequestBody @Valid EnergyControl energyControl){
        energyControl.setTime(LocalDateTime.now(clockWrapper.getClock()));
        if (energyControl.getUsageSwitch().equals(UsageSwitch.SWITCH_ONLY_ESSENTIAL.toString())) {
            logger.info("Only essential usages will be allowed for region : " + energyControl.getRegion() + ".");

            return new ResponseEntity<>(energyControlRepository.save(energyControl), HttpStatus.CREATED);

        }
        else if(energyControl.getUsageSwitch().equals(UsageSwitch.SWITCH_ALL_CONSUMPTION.toString())){
            logger.info("All usages will be allowed for region : " + energyControl.getRegion() + ".");

            return new ResponseEntity<>(energyControlRepository.save(energyControl), HttpStatus.CREATED);
        }
        logger.warning("Error in request.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/viewControlHistory")
    public void viewSwitchUsage(){
        for(EnergyControl e : energyControlRepository.findAll()){
            logger.info(e.toString());
        }
    }


}
