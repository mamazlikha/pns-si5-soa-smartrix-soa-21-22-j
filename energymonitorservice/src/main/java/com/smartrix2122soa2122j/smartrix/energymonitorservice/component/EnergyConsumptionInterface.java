package com.smartrix2122soa2122j.smartrix.energymonitorservice.component;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.ElectricComponent;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.EnergyControl;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.Measure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.UsageSwitch;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.ElectricComponentRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.EnergyControlRepository;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Component
public class EnergyConsumptionInterface {

    private final Logger logger = Logger.getLogger(EnergyConsumptionInterface.class.getName());

    @Autowired
    private ElectricComponentRepository electricComponentRepository;

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private EnergyControlRepository energyControlRepository;

    @Autowired
    private ClockWrapper clockWrapper;

    public void recordCustomerMeasures(String customerId) {
        logger.info("Adding new measures for : " + customerId);
        List<ElectricComponent> components = electricComponentRepository.findAllByCustomerId(customerId);
        if (components.isEmpty()) {
            components = electricComponentRepository.findAllByCustomerName(customerId);
        }
        for (ElectricComponent c : components) {
            recordConsumption(c, LocalDateTime.now(this.clockWrapper.getClock()));
        }
    }

    public void recordConsumption(ElectricComponent electricComponent, LocalDateTime recordTime) {
        Measure measure = new Measure(recordTime, electricComponent.getCustomerId(), electricComponent.getEnergyUsed(),
                electricComponent.getRegion(), electricComponent.getId());
        if (electricComponent.getUsageType().equals(UsageSwitch.ESSENTIAL.toString())) {
            measureRepository.save(measure);
        } else {
            List<EnergyControl> energyControlHistory = energyControlRepository
                    .findByRegion(electricComponent.getRegion());
            if (energyControlHistory.isEmpty()) {
                measureRepository.save(measure);
                return;
            }
            if (energyControlHistory.get(energyControlHistory.size() - 1).getUsageSwitch()
                    .equals(UsageSwitch.SWITCH_ALL_CONSUMPTION.toString())) {
                measureRepository.save(measure);
            } else {
                electricComponent.setIsOn(false);
                electricComponentRepository.save(electricComponent);
            }
        }
    }

}
