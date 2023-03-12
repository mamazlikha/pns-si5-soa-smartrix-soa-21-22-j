package com.smartrix2122soa2122j.smartrix.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.components.ClockWrapper;
import com.smartrix2122soa2122j.smartrix.entity.Customer;
import com.smartrix2122soa2122j.smartrix.entity.PrivatePerson;
import com.smartrix2122soa2122j.smartrix.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.repository.CustomerRepository;

import com.smartrix2122soa2122j.smartrix.services.CustomerCareWS;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerRegistration {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ClockWrapper clockWrapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CustomerCareWS customerCareWS;

    private final Logger logger = Logger.getLogger(CustomerRegistration.class.getName());


    private final ObjectMapper objectMapper;

    public CustomerRegistration(Jackson2ObjectMapperBuilder mapperBuilder){
        this.objectMapper=mapperBuilder.build();
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> registerCustomer(@Valid @RequestBody Customer customer) {

        if ((customer.getId() != null && customerRepository.existsById(customer.getId()))
                || (customer.getName() != null && customerRepository.existsByName(customer.getName()))) {
            return ResponseEntity.badRequest().build();
        }

        customer.setSubscriptionDateTime(LocalDateTime.now(clockWrapper.getClock()));

        customer=customerRepository.save(customer);

        if(customer.getType().equals("PRIVATE_PERSON")){
            PrivatePerson person = (PrivatePerson) customer;
            customerCareWS.addPrivateComponents(person);//TODO renvoyer les ids des composants ?
        }

        try {
            String json = objectMapper.writeValueAsString(customer);
            logger.info("Sending customer data to EmailService && AccountingService...");
            rabbitTemplate.convertAndSend("mail-exchange", "mail.accounting.info", json);
        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PostMapping("/customers/{name}/battery")
    public void installBattery(@PathVariable("name") String customerName, @RequestBody DTO.Battery battery){
        Customer customer = this.customerRepository.findByName(customerName).orElseThrow();

        battery.setCustomerId(customer.getId());
        this.customerCareWS.installBattery(battery);


    }

    @GetMapping("/customers")
    public List<Customer> fetchCustomers() {
        return customerRepository.findAll();
    }


}
