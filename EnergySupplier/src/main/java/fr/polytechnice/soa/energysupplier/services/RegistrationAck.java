package fr.polytechnice.soa.energysupplier.services;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

@Component
public class RegistrationAck {
    

    private final Logger logger=Logger.getLogger(RegistrationAck.class.getName());
    public void listenAck(String supplierStr){
        this.logger.info(supplierStr);

      

        
    }
}
