package com.smartrix2122soa2122j.smartrix.reserveenergy.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.reserveenergy.entity.ReserveAddingEnergyRequest;
import com.smartrix2122soa2122j.smartrix.reserveenergy.entity.Energy;
import com.smartrix2122soa2122j.smartrix.reserveenergy.repository.ReserveEnergyRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class ConsumeEnergy {

    @Value("${topic.exchange.reserve.name}")
    private String topicExchangeName;

    private final Logger logger = Logger.getLogger("[ConsumeEnergy] " + ConsumeEnergy.class.getName());

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ReserveEnergyRepository energyReserve;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEnergy(String request) {
        try {
            ReserveAddingEnergyRequest requestedQuantity = objectMapper.readValue(request, ReserveAddingEnergyRequest.class);

            List<Energy> allEnergy = energyReserve.findAll();

            if (allEnergy.isEmpty()){

                logger.info("There is nothing in the reserve !\n");

            } else {
                Energy stockEnergy = allEnergy.get(0);

                if (stockEnergy.getQuantity() >= requestedQuantity.getEnergyRequested()) {

                    stockEnergy.setQuantity(stockEnergy.getQuantity() - requestedQuantity.getEnergyRequested());

                    energyReserve.save(stockEnergy);

                    ReserveAddingEnergyRequest response = new ReserveAddingEnergyRequest();

                    response.setRegion(requestedQuantity.getRegion());

                    response.setEnergyRequested(requestedQuantity.getEnergyRequested());

                    rabbitTemplate.convertAndSend(topicExchangeName, "smartrix.reserve.get.energy", objectMapper.writeValueAsString(response));
                }

                else {
                    logger.info("The requested quantity of energy is more than what the reserve contains");
                }
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
