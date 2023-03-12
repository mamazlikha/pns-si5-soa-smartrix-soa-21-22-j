package com.smartrix2122soa2122j.smartrix.electricmeterregister.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import com.smartrix2122soa2122j.smartrix.electricmeterregister.component.SelfSufficientBean;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.Battery;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.entity.CustomerProductionConsumption;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.repository.BatteryRepository;
import com.smartrix2122soa2122j.smartrix.electricmeterregister.service.ProductionConsumptionInterceptorWS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductionConsumptionInterceptorController {

    @Autowired
    private ProductionConsumptionInterceptorWS productionConsumptionInterceptorWS;

    @Autowired
    private SelfSufficientBean selfSufficientBean;

    @Autowired
    private BatteryRepository batteryRepository;

    private final Logger logger = Logger.getLogger(ProductionConsumptionInterceptorController.class.getName());

    @PostMapping("/customer/battery")
    public Battery registerBattery(@Valid @RequestBody Battery battery) {
        return this.batteryRepository.save(battery);
    }

    @PostMapping("/production-measure")
    public void productionConsumptionIntercept(@RequestBody @Valid CustomerProductionConsumption productionConsumption)
            throws IllegalArgumentException {

        logger.info("saving production");

        if (productionConsumption == null) {
            throw new IllegalArgumentException("request body is empty");
        }
        logger.info("received object : " + productionConsumption.toString());

        productionConsumptionInterceptorWS.handleMeasures(productionConsumption);

        productionConsumptionInterceptorWS.saveProduction(productionConsumption.getProduction());

        
    }

    @GetMapping("/is-autarky/{customerId}")
    public String amIAutarky(@PathVariable("customerId") String customerId) {

        return selfSufficientBean.amIAutarky(customerId) ? "Customer of id  " + customerId + " is autarky!\n"
                : "Customer of id  " + customerId + " is not autarky!\n";
    }

    @GetMapping("/is-region-autarky/{region}")
    public String isRegionAutarky(@PathVariable("region") String region) {
        return selfSufficientBean.isRegionAutarky(region) ? "Region " + region + " is autarky!\n"
                : "Region " + region + " is not autarky!\n";
    }
}
