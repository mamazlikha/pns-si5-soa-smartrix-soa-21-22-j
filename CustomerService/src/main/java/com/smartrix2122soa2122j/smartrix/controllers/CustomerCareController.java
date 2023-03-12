package com.smartrix2122soa2122j.smartrix.controllers;

import java.util.logging.Logger;

import com.smartrix2122soa2122j.smartrix.entity.Customer;
import com.smartrix2122soa2122j.smartrix.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.repository.CustomerRepository;
import com.smartrix2122soa2122j.smartrix.services.CustomerCareWS;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
public class CustomerCareController {

    @Autowired
    private CustomerCareWS customerCareWS;

    @Autowired
    private CustomerRepository customerRepository;

    private final Logger logger = Logger.getLogger(CustomerCareController.class.getName());

    @GetMapping("/customers/{name}/consumption/graph/{graphType}")
    public ResponseEntity<DTO.OrderedConsumptionGraph> getConsumptionGraph(@PathVariable("name") String customerName,
                                                                                      @PathVariable("graphType") String graphType) {
        Customer customer = this.customerRepository.findByName(customerName).orElseThrow();
        return this.customerCareWS.getConsumptionGraph(customer, graphType);

    }



    @GetMapping("/customers/{name}/bills/year/{year}/month/{month}")
    public ResponseEntity<DTO.MonthlyBill> getBillForMonth(@PathVariable("name") String customerName,
                                                           @PathVariable("year") String year, @PathVariable("month") String month) {
        Customer customer = this.customerRepository.findByName(customerName).orElseThrow();

        return this.customerCareWS.getBillForMonth(customer, year, month);
    }
    @GetMapping("/customers/{name}/bills/prevision")
    public ResponseEntity<DTO.MonthlyBill> computeAmountDueForThisMonth(@PathVariable("name") String customerName){
        Customer customer = this.customerRepository.findByName(customerName).orElseThrow();

        return this.customerCareWS.computeAmountDueForThisMonth(customer);

    }

}
