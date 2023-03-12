package com.smartrix2122soa2122j.smartrix.controllers;

import java.util.logging.Logger;

import com.smartrix2122soa2122j.smartrix.entity.MonthlyBill;
import com.smartrix2122soa2122j.smartrix.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.repositories.MonthlyBillRepository;
import com.smartrix2122soa2122j.smartrix.services.BillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillController {
    
    @Autowired
    private MonthlyBillRepository monthlyBillRepository;



    @Autowired
    private BillService billService;
    private Logger logger = Logger.getLogger(BillController.class.getName());

    @GetMapping("/bills/customer/{customerId}/year/{year}/month/{month}")
    public ResponseEntity<MonthlyBill> getBill(@PathVariable("customerId") String customerId, @PathVariable("year") int year, @PathVariable("month") int month){

        var optBill=this.monthlyBillRepository.findByCustomerIdAndMonth(customerId, String.format("%d-%02d",year, month));
        return ResponseEntity.of(optBill);
    }

    @GetMapping("/bills/customer/{customerId}/this-month")
    public ResponseEntity<MonthlyBill> computeAmountDueForThisMonth(@PathVariable("customerId") String customerId){
        
        return this.billService.computeAmountDueForThisMonth(customerId);

    }


    @PostMapping("/bills/customer/energy/sale")
    public void computePurchaseOfEnergyFromCustomers(@RequestBody DTO.MonthlyProductionSoldList monthlyProductionSoldList){
        this.billService.computePurchaseOfEnergyFromCustomers(monthlyProductionSoldList.productions());
    }




    @PostMapping("/bills/customers/monthly")
    public void computeBillForAllCustomers(@RequestBody DTO.CustomersMonthlyConsumptionsList customersMonthlyConsumptionsList){
        this.billService.computeBills(customersMonthlyConsumptionsList.consumptions());


    }
}
