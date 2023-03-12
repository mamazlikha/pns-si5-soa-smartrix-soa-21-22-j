package com.smartrix2122soa2122j.smartrix.services;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.entity.Customer;
import com.smartrix2122soa2122j.smartrix.entity.Energy;
import com.smartrix2122soa2122j.smartrix.entity.EnergySellRequest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ElectricityBrokerWS implements IElectricityBroker {


    @Value("${topic.exchange.reserve.name}")
    private String topicExchangeName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    private Logger logger=Logger.getLogger( ElectricityBrokerWS.class.getName());

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void sellElectricity(Customer customer, Energy energy) {

        logger.info("selling energy ...\n");
        try {

            EnergySellRequest request = new EnergySellRequest();

            request.setEnergy(energy);

            request.setCustomerID(customer.getId());


            rabbitTemplate.convertAndSend(topicExchangeName, "smartrix-accounting-energy-sold-messaging", mapper.writeValueAsString(request));

            kafkaTemplate.send(topicExchangeName, mapper.writeValueAsString(request));

            // rabbitTemplate.convertAndSend(topicExchangeName, "smartrix.energy.reserve.messaging", mapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
