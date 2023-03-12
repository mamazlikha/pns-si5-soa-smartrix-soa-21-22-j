package com.smartrix2122soa2122j.smartrix.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import com.smartrix2122soa2122j.smartrix.components.ClockWrapper;
import com.smartrix2122soa2122j.smartrix.components.TransferMoney;
import com.smartrix2122soa2122j.smartrix.entity.MonthlyBill;
import com.smartrix2122soa2122j.smartrix.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.entity.dto.DTO.CustomerMonthlyConsumption;
import com.smartrix2122soa2122j.smartrix.repositories.MonthlyBillRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Service
public class BillService {

    @Value("${monitor.url}")
    private String energyMonitorUrl;

    @Value("${scheduler.activated}")
    private boolean schedulerActivated;
    private final RestTemplate restTemplate;
    private ClockWrapper clockWrapper;

    private Logger logger = Logger.getLogger(BillService.class.getName());

    public static final BigDecimal electricityPrice = new BigDecimal("0.1");// constant in application.properties TODO

    private MonthlyBillRepository monthlyBillRepository;
    private TransferMoney transferMoney;

    @Autowired
    public BillService(RestTemplateBuilder restTemplateBuilder, ClockWrapper clockWrapper,
            MonthlyBillRepository monthlyBillRepository, TransferMoney transferMoney) {
        this.restTemplate = restTemplateBuilder.build();
        this.clockWrapper = clockWrapper;
        this.monthlyBillRepository = monthlyBillRepository;
        this.transferMoney = transferMoney;

    }

    public ResponseEntity<MonthlyBill> computeAmountDueForThisMonth(@PathVariable("customerId") String customerId) {

        var thisMonthConsumptionResponse = this.restTemplate.getForEntity(
                energyMonitorUrl + "/consumption/{customerId}/this-month/until-now", CustomerMonthlyConsumption.class,
                customerId);

        var thisMonthConsumption = thisMonthConsumptionResponse.getBody();
        if (thisMonthConsumptionResponse.getStatusCode() == HttpStatus.OK && thisMonthConsumption != null) {

            var bill = new MonthlyBill();
            bill.setCustomerId(customerId);
            bill.setEnergyUsed(thisMonthConsumption.energyUsed());
            bill.setToPay(BillService.electricityPrice.multiply(new BigDecimal(thisMonthConsumption.energyUsed())));
            var today = LocalDate.now(this.clockWrapper.getClock());

            bill.setDate(today);

            bill.setMonth(today.format(DateTimeFormatter.ofPattern("u-MM")));

            return ResponseEntity.ok(bill);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Async
    public void computePurchaseOfEnergyFromCustomers(List<DTO.MonthlyProductionSold> productions) {
        LocalDateTime today = LocalDateTime.now(this.clockWrapper.getClock());

        logger.info("Paying customers we buy energy from");

        for (var production : productions) {
            this.transferMoney.creditCustomer(production.customerId(),
                    electricityPrice.multiply(new BigDecimal(production.energy())).doubleValue());
        }
    }

    @Async
    public void computeBills(List<CustomerMonthlyConsumption> consumptions) {
        LocalDateTime today = LocalDateTime.now(this.clockWrapper.getClock());

        var month = today.minusMonths(1).toLocalDate().format(DateTimeFormatter.ofPattern("u-MM"));
        logger.info("Computing The Bills on " + today.format(DateTimeFormatter.ISO_DATE_TIME));

        for (var customerconsumption : consumptions) {

            var monthlyBill = new MonthlyBill();

            monthlyBill.setCustomerId(customerconsumption.customerId());
            monthlyBill.setDate(today.toLocalDate());

            monthlyBill.setMonth(month);
            monthlyBill.setEnergyUsed(customerconsumption.energyUsed());
            monthlyBill.setToPay(electricityPrice.multiply(new BigDecimal(customerconsumption.energyUsed())));

            logger.info("Bill " + monthlyBill);
            monthlyBill = this.monthlyBillRepository.save(monthlyBill);

            logger.info("Bill " + monthlyBill);

            this.transferMoney.debitCustomer(customerconsumption.customerId(), monthlyBill.getToPay().doubleValue());
            // TODO send to emailservice so emailservice send the bill to the customers
        }

    }

}
