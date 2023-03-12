package com.smartrix2122soa2122j.smartrix.electricmeterregister.service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.component.ClockWrapper;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.CustomerLocalProduction;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.CustomerProductionConsumption;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.CustomerProductionSold;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.Measure;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.repository.BatteryRepository;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.repository.ExcessLocalProductionRepository;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.repository.LocalProductionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductionConsumptionInterceptorWS {

    private final ObjectMapper objectMapper;
   
    @Value("${energy.monitor.service.address}")
    private String energyMonitorService;

    private final Logger logger = Logger.getLogger(ProductionConsumptionInterceptorWS.class.getName());

    @Autowired
    private BatteryRepository batteryRepository;

    @Autowired
    private ExcessLocalProductionRepository excessLocalProductionRepository;
    @Autowired
    private LocalProductionRepository productionRepository;


    private final RestTemplate restTemplate;

    @Autowired
    private ClockWrapper clockWrapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public ProductionConsumptionInterceptorWS(RestTemplateBuilder restTemplateBuilder, Jackson2ObjectMapperBuilder objectMapperBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper=objectMapperBuilder.build();
    }

    public void handleMeasures(CustomerProductionConsumption productionConsumption)  {

        String customerId = productionConsumption.getConsumption().getCustomerId();

        var optBattery = batteryRepository.findByCustomerId(customerId);

        
        int consumption = productionConsumption.getConsumption().getEnergyUsed();
        int production = productionConsumption.getProduction() != null
                ? productionConsumption.getProduction().getQuantity()
                : 0;
        if (optBattery.isPresent()) {
            var battery = optBattery.get();

            int nextBatteryLevel = Math.min(battery.getMaximumCapacity(),
                    Math.max(0, battery.getCurrentLevel() - consumption + production));

            int excessProduction = Math.max(0,
                    battery.getCurrentLevel() - consumption + production - battery.getMaximumCapacity());

            int remainingConsumption = Math.abs(Math.min(0, battery.getCurrentLevel() - consumption + production));

            battery.setCurrentLevel(nextBatteryLevel);

            this.batteryRepository.save(battery);


            if(excessProduction > 0){
                
                this.excessLocalProductionRepository.save(new CustomerProductionSold(excessProduction, LocalDateTime.now(clockWrapper.getClock()), customerId,productionConsumption.getConsumption().getRegion()  ));

                try {
                    this.kafkaTemplate.send("smartrix-reserve-messaging", this.objectMapper.writeValueAsString(new DTO.Production(excessProduction)) );
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
    
            }

            this.forwardMeasuresToMonitor(new Measure(productionConsumption.getConsumption().getTimestamp(), customerId,  remainingConsumption, productionConsumption.getConsumption().getRegion()));

        } else {
            this.forwardMeasuresToMonitor(productionConsumption.getConsumption());
        }

    }

    public void saveProduction(CustomerLocalProduction production) {
        if (production != null) {

            productionRepository.save(production);

        }
    }

    public void forwardMeasuresToMonitor(Measure measure) {
        restTemplate.postForObject(energyMonitorService + "/measures", measure, Measure.class);
    }

}
