package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.controllers;

import java.util.List;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component.EnergyOrchestratorBean;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.Supplier;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrchestratorController {
    
    @Autowired
    private EnergyOrchestratorBean energyOrchestrator;

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping("/orchestrator/compute")
    public void computeLatestConsumption(){
        this.energyOrchestrator.computeCustomersConsumptionDemo();
    }


    @GetMapping("/admin/suppliers")
    public List<Supplier> fetchRegisteredSuppliers(){
        return supplierRepository.findAll();
    }
}
