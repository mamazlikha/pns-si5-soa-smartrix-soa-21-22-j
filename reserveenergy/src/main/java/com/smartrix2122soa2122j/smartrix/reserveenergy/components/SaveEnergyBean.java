package com.smartrix2122soa2122j.smartrix.reserveenergy.components;

import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.reserveenergy.entity.Energy;
import com.smartrix2122soa2122j.smartrix.reserveenergy.entity.Production;
import com.smartrix2122soa2122j.smartrix.reserveenergy.repository.ReserveEnergyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SaveEnergyBean {


    private final Logger logger = Logger.getLogger(SaveEnergyBean.class.getName());


    @Autowired
    private ReserveEnergyRepository energyRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(containerFactory = "energySellRequestKafkaListenerContainerFactory", topics = "smartrix-reserve-messaging")
    public void saveEnergy(Production production) {

        logger.info("saving energy ...");
        logger.info("Energy sell request : " + production);



        if (energyRepository.count() == 0) {
            Energy energy = new Energy();
            energy.setQuantity(production.energy());
            energyRepository.save(energy);

        } else {
            Energy energy = energyRepository.findAll().get(0);
            energy.setQuantity(energy.getQuantity() + production.energy());
            energyRepository.save(energy);

        }

        logger.info("the quantity " + production.energy() + " has been successfully stored !");


    }
}

