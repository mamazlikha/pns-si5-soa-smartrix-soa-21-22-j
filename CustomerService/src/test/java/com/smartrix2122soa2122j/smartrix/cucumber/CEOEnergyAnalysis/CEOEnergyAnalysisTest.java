package com.smartrix2122soa2122j.smartrix.cucumber.CEOEnergyAnalysis;

import com.smartrix2122soa2122j.smartrix.SmartrixApplication;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(value = Cucumber.class)
@ContextConfiguration(classes = SmartrixApplication.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features/CEOEnergyAnalysis/",
        extraGlue = "com.smartrix2122soa2122j.smartrix")
public class CEOEnergyAnalysisTest {
}
