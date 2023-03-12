package com.smartrix2122soa2122j.smartrix.controllers;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import com.smartrix2122soa2122j.smartrix.components.ClockWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeController {

    @Autowired
    private ClockWrapper wrapper;
    /*@Autowired
    private BankDetailsRepository bankDetailsRepository;
    @Autowired
    private TransferMoney transferMoney;*/

    @GetMapping("/admin/time/{clockReference}")
    public void changeClock(@PathVariable("clockReference") String clockStr) {
        this.wrapper.setClock(Clock.fixed(Instant.parse(clockStr), ZoneOffset.ofHours(0)));
    }
    /*static int amount=10;
    @GetMapping("/test")
    public int testClock() {

        transferMoney.creditCustomer("c2DTO",amount);
        return amount+=10;
    }*/

}
