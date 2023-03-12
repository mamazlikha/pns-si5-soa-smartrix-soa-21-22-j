package com.smartrix2122soa2122j.smartrix.energymonitorservice.interfaces;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MeasureRepository;

public interface IMonitor {

    void setMeasureRepository(MeasureRepository measureRepository); // For Cucumber Test https://stackoverflow.com/questions/41347764/spring-mockbean-not-injected-with-cucumber


}
