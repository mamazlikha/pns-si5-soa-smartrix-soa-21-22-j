package com.smartrix2122soa2122j.smartrix.energymonitorservice.component;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MeasureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrchestratorInterface {

    private static final long FIXED_RATE = 100_000; // millisecond

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private ClockWrapper clockWrapper;

    private final Logger logger=Logger.getLogger(OrchestratorInterface.class.getName());




    public DTO.EnergyConsumedPerRegionResponse latestEnergyConsumedByRegion() {
        this.logger.info("receive request from Orchestrator ");

        var now = LocalDateTime.now(clockWrapper.getClock());
        var before = now.minusSeconds(FIXED_RATE / 1000);

        return new DTO.EnergyConsumedPerRegionResponse( measureRepository.getConsumptionsGroupedByRegion (before, now));
    }

}
