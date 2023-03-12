package com.smartrix2122soa2122j.smartrix.components;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
public class ElectricityBrokerInterface {

    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Autowired
    private ClockWrapper clockWrapper;

    @Autowired
    private TransferMoney transferMoney;

    private final ObjectMapper mapper;

    @Autowired
    public ElectricityBrokerInterface(Jackson2ObjectMapperBuilder builder) {
        this.mapper = builder.build();

    }

    private final Logger logger = Logger.getLogger("[AccountingService] " + ElectricityBrokerInterface.class.getName());

    public void creditCustomer(String electricityBrokerRequest) {
        this.logger.info("Receiving brokerRequest " + electricityBrokerRequest);
        requestToBank(electricityBrokerRequest);
    }

    public void debitCustomer(String electricityBrokerRequest) {
        requestToBank(electricityBrokerRequest);
    }

    private void requestToBank(String electricityBrokerRequest) {
        
        try {
            ElectricityBrokerRequest eRequest = mapper.readValue(electricityBrokerRequest,
                    ElectricityBrokerRequest.class);

            transferMoney.creditCustomer(eRequest.customerID(), eRequest.energy().quantity());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public record ElectricityBrokerRequest(String customerID, Energy energy) {

    }

    /**
     * Energy
     */
    public record Energy(double quantity, double price) {
    }

}
