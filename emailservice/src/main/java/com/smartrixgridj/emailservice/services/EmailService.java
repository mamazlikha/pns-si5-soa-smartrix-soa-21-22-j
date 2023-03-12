package com.smartrixgridj.emailservice.services;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrixgridj.emailservice.entities.Customer;
import com.smartrixgridj.emailservice.repositories.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private CustomerRepository customerRepository;

    private final ObjectMapper objectMapper;

    private final Logger logger = Logger.getLogger(EmailService.class.getName());

    public EmailService(Jackson2ObjectMapperBuilder mapperBuilder) {
        this.objectMapper = mapperBuilder.build();
    }

    public void sendEmail(String regionName) {

        this.customerRepository.findByRegion(regionName).forEach(customer -> {
            logger.info("Sending email to customer " + customer);
        });
    }

    public void dupCustomer(String customer) {
        logger.info("Receiving customer : " + customer);

        try {
            Customer custo = objectMapper.readValue(customer, Customer.class);
            this.customerRepository.save(custo);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
