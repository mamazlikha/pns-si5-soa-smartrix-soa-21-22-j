package com.smartrix2122soa2122j.smartrix.services;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public interface IContactEnergyOrchestrator {
    void sendEnergyConsumedOnFixRate(LocalDateTime end, TemporalAmount difference);
}
