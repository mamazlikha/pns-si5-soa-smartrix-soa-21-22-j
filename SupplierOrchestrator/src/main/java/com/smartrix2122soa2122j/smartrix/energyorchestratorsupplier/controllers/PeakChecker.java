package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.controllers;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.Supplier;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.EnergyConsumptionLogRepository;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.SupplierRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class PeakChecker {

    @Autowired
    private EnergyConsumptionLogRepository logRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    private Logger logger = Logger.getLogger(PeakChecker.class.getName());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/regions/summary")
    public List<RegionSummary> getRegionSummaries() {

        List<RegionSummary> result = new ArrayList<>();

        logRepository.findAll().forEach(log -> {

            var suppliers = supplierRepository.findByRegion(log.getRegion());
            result.add(
                    new RegionSummary(log.getRegion(), suppliers.stream().mapToInt(Supplier::getMaximumCapacity).sum(),
                            suppliers.stream().mapToInt(Supplier::getCurrentProduction).sum(), log.getEnergyT1(),
                            log.getEnergyT2()));
        });
        return result;
    }

    @GetMapping("/regions/{regionName}/communication")
    public void sendEmailToRegion(@PathVariable("regionName") String region) {
        logger.info("Sending email to every user in "+ region);
        this.rabbitTemplate.convertAndSend("mail-exchange", "send.email.region", region);

    }

    /**
     * RegionSummary
     */
    public record RegionSummary(String region, int maximumProduction, int currentProduction, int consumptionT1,
            int consumptionT2) {
    }

}
