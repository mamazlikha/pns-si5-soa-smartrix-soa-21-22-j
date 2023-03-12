package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component;

import java.util.Optional;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.ConsumptionEvolutionInRegion;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.Supplier;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.EnergyConsumptionLogRepository;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EnergyOrchestratorBean {

    private static final long FIXED_RATE = 100_000; // millisecond

    @Autowired
    private EnergyConsumptionLogRepository energyConsumptionLog;

    @Autowired
    private SupplierRepository supplierRepository;




    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Value("${queue.name}")
    private String queueName;

    @Value("${scheduling.enabled}")
    private boolean isEnabled;



    private RestTemplate restTemplate;

    @Autowired
    private ClockWrapper clockWrapper;

    @Autowired
    private ContactEnergySupplierBean contactEnergySupplierBean;

    @Value("${consumption.monitor.url}")
    private String consumptionMonitorUrl;

    private final Logger logger = Logger.getLogger(EnergyOrchestratorBean.class.getSimpleName());

    private ObjectMapper objectMapper ;

    public EnergyOrchestratorBean(RestTemplateBuilder restTemplateBuilder, Jackson2ObjectMapperBuilder objectMapperBuilder){
        this.restTemplate=restTemplateBuilder.build();
        this.objectMapper= objectMapperBuilder.build();
    }

    @Scheduled(fixedRate = FIXED_RATE)
    public void computeCustomersConsumption() {
        if(this.isEnabled){


            var response= this.restTemplate.getForEntity(this.consumptionMonitorUrl+"/regions/consumptions/latest", DTO.EnergyConsumedPerRegionResponse.class);

            if(response.getStatusCode().value() <300 && response.getStatusCode().value() >=200  && response.getBody() != null){
                this.orchestrateGlobally(response.getBody());
            }

        }
        

    }

    public void computeCustomersConsumptionDemo(){
        var response= this.restTemplate.getForEntity(this.consumptionMonitorUrl+"/regions/consumptions/latest", DTO.EnergyConsumedPerRegionResponse.class);

        if(response.getStatusCode().value() < 300 && response.getStatusCode().value() >=200  && response.getBody() != null){
            this.orchestrateGlobally(response.getBody());
        }
    }



    private void orchestrateGlobally(DTO.EnergyConsumedPerRegionResponse response) {

        this.logger.info("***** orchestrating suppliers" +response);

        for (var pairRegionEnergy : response.consumptions()) {//TODO après rendu traiter le cas où la liste est vide ou certaines régions n'ont pas de consommation


            var optRegion = energyConsumptionLog.findByRegion(pairRegionEnergy.region());

            if (optRegion.isPresent()) {
                
                var regionEvolution = optRegion.get();
                this.logger.info("Region "+ regionEvolution.getRegion()+" is present");

                if (regionEvolution.getEnergyT2() <= -1) {
                    regionEvolution.setEnergyT2(pairRegionEnergy.energyUsed());
                } else {
                    regionEvolution.setEnergyT1(regionEvolution.getEnergyT2());
                    regionEvolution.setEnergyT2(pairRegionEnergy.energyUsed());
                }

                regionEvolution = energyConsumptionLog.save(regionEvolution);

                
                contactEnergySupplierBean.manageConsumptionForRegion(regionEvolution);
            }

            else {
                this.logger.info("Region "+ pairRegionEnergy.region()+" is not present");
                var regionEvolution = new ConsumptionEvolutionInRegion();
                regionEvolution.setEnergyT1(pairRegionEnergy.energyUsed());

                regionEvolution.setEnergyT2(-1);

                regionEvolution.setRegion(pairRegionEnergy.region());

               var result= energyConsumptionLog.save(regionEvolution);

               this.logger.info(result.getRegion() +" "+ regionEvolution);
            }
        }

    }


    public void updateSupplier(String response){
        try {
            logger.info("updating supplier current production ...\n");
            Supplier returnedSupplier = objectMapper.readValue(response, Supplier.class);

            Optional<Supplier> supplierInDBOpt = supplierRepository.findByName(returnedSupplier.getName());

            if(supplierInDBOpt.isPresent()){
                Supplier supplierInDB = supplierInDBOpt.get();

                supplierInDB.setCurrentProduction(returnedSupplier.getCurrentProduction());


                supplierRepository.save(supplierInDB);

                logger.info("current production of supplier " + supplierInDB.getName() + " is now " + supplierInDB.getCurrentProduction());
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}