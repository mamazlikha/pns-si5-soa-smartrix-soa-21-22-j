package com.smartrix2122soa2122j.smartrix.electricmeterregister.entity;

import org.springframework.data.annotation.Id;

public class CustomerAutarky {

    @Id
    private String customerId;
    private boolean isInAutarky;
}



