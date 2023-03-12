package fr.polytechnice.soa.energysupplier.controllers;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.polytechnice.soa.energysupplier.models.AdjustmentRequest;
import fr.polytechnice.soa.energysupplier.models.EnergySuppliedRequest;
import fr.polytechnice.soa.energysupplier.services.SupplyService;


@Component
public class SupplyController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger= Logger.getLogger(SupplyController.class.getName());

    @Autowired
    private SupplyService supplyService;

    public void getEnergyCurrentlySupplied(String eventRequest){
        try {
            EnergySuppliedRequest energySuppliedRequest = objectMapper.readValue(eventRequest, EnergySuppliedRequest.class);
            supplyService.getEnergyCurrentlySupplied(energySuppliedRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void getProductionCapacity(String eventRequest){
        logger.info("============ getProductionCapacity ============");
        try {
            EnergySuppliedRequest energySuppliedRequest = objectMapper.readValue(eventRequest, EnergySuppliedRequest.class);
            supplyService.getProductionCapacity(energySuppliedRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void adjustEnergyProduced(String  adjustmentRequest){
        logger.info("Received adjustmentRequest " +adjustmentRequest);
        try {
            AdjustmentRequest request=objectMapper.readValue(adjustmentRequest, AdjustmentRequest.class);

            supplyService.adjustEnergySupplied(request);
        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }
    }

}
