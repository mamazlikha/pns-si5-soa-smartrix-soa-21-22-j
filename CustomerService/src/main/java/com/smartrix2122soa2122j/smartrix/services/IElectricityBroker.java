package com.smartrix2122soa2122j.smartrix.services;

import com.smartrix2122soa2122j.smartrix.entity.Customer;
import com.smartrix2122soa2122j.smartrix.entity.Energy;

public interface IElectricityBroker {


    public void sellElectricity(Customer customer, Energy energy);

}
