package com.smartrix2122soa2122j.smartrix.energymonitorservice.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.AccountingInterface;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO.CustomerMonthlyConsumption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountingInterfaceController {

    @Autowired
    private AccountingInterface accountingInterface;

    @GetMapping("/consumptions/month/until/{endTime}")
    public CustomersMonthlyConsumptionsList computeMonthlyConsumption(@PathVariable("endTime") String endDateTime) {
       return new  CustomersMonthlyConsumptionsList(this.accountingInterface.computeMonthlyConsumption(LocalDateTime.parse(endDateTime)));
    }

    @GetMapping("/consumption/{customerId}/this-month/until-now")
    public CustomerMonthlyConsumption computeThisMonthConsumptionForCustomer(@PathVariable("customerId") String customerId){
        return this.accountingInterface.computeThisMonthConsumption(customerId);
        
    }


    /**
     * CustomersMonthlyConsumptionsList
List<CustomerMonthlyConsumption> consumptions     */
    public record CustomersMonthlyConsumptionsList(List<CustomerMonthlyConsumption> consumptions) {
    }

}
