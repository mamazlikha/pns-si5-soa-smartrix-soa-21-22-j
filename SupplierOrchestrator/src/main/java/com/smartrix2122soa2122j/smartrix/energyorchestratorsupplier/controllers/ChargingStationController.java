package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.controllers;

import java.util.logging.Logger;

import javax.validation.Valid;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.CarChargingRequest;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.ChargingStation;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.ChargingStationRepository;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.services.CarChargingWS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargingStationController {

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private CarChargingWS carChargingWS;

    private Logger logger=Logger.getLogger("[ChargingStationController]");

    @PostMapping("/chargingStations")
    public ResponseEntity<ChargingStation> registerChargingStation(@Valid @RequestBody ChargingStation chargingStation){

        return new ResponseEntity<>(chargingStationRepository.save(chargingStation) ,HttpStatus.CREATED);


    }

    @PostMapping("/plugCar")
    @ResponseBody
    public ResponseEntity<String> requestCarEnergy(@RequestBody @Valid CarChargingRequest carChargingRequest){
        if(carChargingWS.plugCar(carChargingRequest)){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Request of charging car accepted");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Request of charging car denied, retry later");
        }

    }

}
