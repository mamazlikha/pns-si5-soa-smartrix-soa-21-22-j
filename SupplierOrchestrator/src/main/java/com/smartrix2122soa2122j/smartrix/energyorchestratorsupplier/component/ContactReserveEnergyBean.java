package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.RegionProviders;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.ReserveAddingEnergyRequest;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.Supplier;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.RegionProvidersRepository;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.SupplierRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class ContactReserveEnergyBean {


    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = Logger.getLogger("[ContactReserveEnergyBean] " + ContactReserveEnergyBean.class.getName());

    @Value("${topic.exchange.reserve.name}")
    private String topicExchangeName;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private RegionProvidersRepository regionProvidersRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void getEnergyFromReserve(int quantity, String regionName){
        try {

            ReserveAddingEnergyRequest energyRequest = new ReserveAddingEnergyRequest();

            energyRequest.setEnergyRequested(quantity);

            energyRequest.setRegion(regionName);

            String request = objectMapper.writeValueAsString(energyRequest);

            rabbitTemplate.convertAndSend(topicExchangeName, "smartrix-get-energy-from-reserve", request);

        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }

    }



    public void updateEnergyConsumption(String response){

        try {
            ReserveAddingEnergyRequest energyQuantityReceived = objectMapper.readValue(response, ReserveAddingEnergyRequest.class);

            Optional<RegionProviders> regionProvidersOptional = regionProvidersRepository.findRegionProvidersByRegion(energyQuantityReceived.getRegion());

            if(regionProvidersOptional.isPresent()){


                RegionProviders regionProviders = regionProvidersOptional.get();

                regionProviders.setEnergyReserve(energyQuantityReceived.getEnergyRequested());

                regionProvidersRepository.save(regionProviders);

            } else {

                RegionProviders newRegionProviders = new RegionProviders();

                List<Supplier> regionSuppliers = supplierRepository.findByRegion(energyQuantityReceived.getRegion());

                newRegionProviders.setSuppliers(regionSuppliers);

                newRegionProviders.setEnergyReserve(energyQuantityReceived.getEnergyRequested());

                regionProvidersRepository.save(newRegionProviders);

            }

        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }

    }

}
