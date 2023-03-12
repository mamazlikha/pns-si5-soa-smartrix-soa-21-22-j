package com.smartrix2122soa2122j.smartrix.controllers;

import com.smartrix2122soa2122j.smartrix.entity.Customer;
import com.smartrix2122soa2122j.smartrix.entity.Energy;
import com.smartrix2122soa2122j.smartrix.repository.CustomerRepository;
import com.smartrix2122soa2122j.smartrix.services.ElectricityBrokerWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class ElectricityBrokerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ElectricityBrokerWS electricityBrokerWS;

    private final Logger logger = Logger.getLogger(ElectricityBrokerController.class.getName());

    @PostMapping("/sell-electrity/{name}")
    public void sellElectricity(@PathVariable("name") String name, @Valid @RequestBody Energy energy){
        Optional<Customer> customerOptional  = customerRepository.findByName(name);
        logger.info("finding customer ...\n");
        customerOptional.ifPresent(customer -> electricityBrokerWS.sellElectricity(customer, energy));

    }

}
