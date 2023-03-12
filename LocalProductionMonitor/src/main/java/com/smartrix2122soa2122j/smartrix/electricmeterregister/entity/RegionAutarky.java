package com.smartrix2122soa2122j.smartrix.electricmeterregister.entity;

import org.springframework.data.annotation.Id;

public class RegionAutarky {
    @Id
    private String region;
    private boolean isInAutarky;
}
