package com.smartrix2122soa2122j.smartrix.energymonitorservice.controller;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.ClockWrapper;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.EnergyConsumptionInterface;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.OrchestratorInterface;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.ElectricComponent;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.EnergyControl;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.Measure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.UsageSwitch;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@RestController()
public class ElectricMeterController {

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private DailyAggregateRepository dailyAggregateRepository;

    @Autowired
    private EnergyControlRepository energyControlRepository;

    @Autowired
    private ElectricComponentRepository electricComponentRepository;

    @Autowired
    private EnergyConsumptionInterface energyConsumptionInterface;

    @Autowired
    private DailyGlobalMeasureRepository dailyGlobalMeasureRepository;

    @Autowired
    private ClockWrapper clockWrapper;

    @Autowired
    private OrchestratorInterface orchestratorInterface;
    private final Logger logger = Logger.getLogger(ElectricMeterController.class.getName());

    @PostMapping("/measures")
    public ResponseEntity<Measure> receiveMeasure(@RequestBody @Valid Measure measure){
        if(measure.getUsageType() == null){
            return new ResponseEntity<>(measureRepository.save(measure), HttpStatus.CREATED);
        }
        if(measure.getUsageType().equals(UsageSwitch.NON_ESSENTIAL.toString())){
            List<EnergyControl> energyControlHistory = energyControlRepository.findByRegion(measure.getRegion());
            if(energyControlHistory.isEmpty()){
                return new ResponseEntity<>(measureRepository.save(measure), HttpStatus.CREATED);
            }
            if(energyControlHistory.get(energyControlHistory.size()-1).getUsageSwitch().equals(UsageSwitch.SWITCH_ONLY_ESSENTIAL.toString())){
                return new ResponseEntity<>(HttpStatus.LOCKED);
            }
        }
        return new ResponseEntity<>(measureRepository.save(measure), HttpStatus.CREATED);
    }



    @GetMapping("/customerSendMeasures/{customerId}")
    public void customerSendMeasures(@PathVariable("customerId") String customerId){
        energyConsumptionInterface.recordCustomerMeasures(customerId);
    }

    @GetMapping("/measures/customer/{customerId}/from/{lower}/to/{upper}")
    public ResponseEntity<DTO.DailyCustomerMeasureList> getMeasuresByCustomerId(@PathVariable ("customerId") String customerId, @PathVariable("lower") String lower, @PathVariable("upper")  String upper){
        return new ResponseEntity<>(new DTO.DailyCustomerMeasureList( this.dailyAggregateRepository.findDailyCustomerMeasure(customerId, lower, upper)), HttpStatus.OK);
    }


    @GetMapping("/region-measures/{region}/from/{lower}/to/{upper}")
    public ResponseEntity<DTO.PairRegionEnergy> getMeasuresByRegion(@PathVariable ("region") String region, @PathVariable("lower") String lower, @PathVariable("upper")  String upper){

        logger.info("get Measure by region ...");

        var tmp= this.dailyAggregateRepository.getEnergyUsedInRegionBetween(lower, upper, region);
        logger.info("Result from db for  region="+region+" lower :"+lower+" upper= "+upper +" is "+tmp);
        return new ResponseEntity<>(tmp, HttpStatus.OK);

    }
    @GetMapping("/regions/consumptions/latest")
    public DTO.EnergyConsumedPerRegionResponse getLatestConsumptionsOfRegions(){
        return orchestratorInterface.latestEnergyConsumedByRegion();
    }


    @GetMapping("/viewMeasures")
    public void viewMeasures(){
        for(Measure m : measureRepository.findAll()){
            logger.info(m.toString());
        }
    }

    @PostMapping("/addCustomerComponent")
    public ResponseEntity<DTO.AddComponentRequest> addCustomerComponent(@NotNull @RequestBody @Valid DTO.AddComponentRequest addComponentRequest){
        logger.info("Adding components for customer : " + addComponentRequest.customerId());
        for(ElectricComponent c : addComponentRequest.electricComponents()){
            c.setCustomerId(addComponentRequest.customerId());
            c.setIsOn(false);
            electricComponentRepository.save(c);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/switchCustomerComponent")
    public ResponseEntity<DTO.ComponentSwitchRequest> switchCustomerComponent(@RequestBody @Valid DTO.ComponentSwitchRequest componentSwitchRequest){
        logger.info("Switching component : " + componentSwitchRequest.componentName() + " of customer : " + componentSwitchRequest.customerName() + "(id : " + componentSwitchRequest.customerId() + ") " + componentSwitchRequest.switchOn());

        List<ElectricComponent> components = electricComponentRepository.findAllByCustomerId(componentSwitchRequest.customerId());
        if(components.isEmpty()){
            components = electricComponentRepository.findAllByCustomerName(componentSwitchRequest.customerName());
        }
        for(ElectricComponent e : components){
            if(e.getName().equals(componentSwitchRequest.componentName())){
                e.setIsOn(componentSwitchRequest.switchOn());
                electricComponentRepository.save(e);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }
        logger.info("Component not found");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
