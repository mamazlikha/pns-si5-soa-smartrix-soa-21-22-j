package fr.polytechnice.soa.energysupplier;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import fr.polytechnice.soa.energysupplier.interfaces.IRegistration;
import fr.polytechnice.soa.energysupplier.models.Supplier;

@Component
public class StartUpLogic implements ApplicationRunner{

    @Autowired
    private IRegistration registration;


    @Value("${supplier.company.name}")
    private String supplierName;

    @Value("${supplier.company.maximum.capacity}")
    private int maximumCapacity;

    @Value("${supplier.company.region}")
    private String supplierRegion;

    private final Logger logger=Logger.getLogger(StartUpLogic.class.getName());

    @Override
    public void run(ApplicationArguments args) throws Exception {


        var supplier=new Supplier();

        supplier.setMaximumCapacity(this.maximumCapacity);
        supplier.setName(this.supplierName);

        supplier.setCurrentProduction((this.maximumCapacity *50)/100);

        supplier.setRegion(this.supplierRegion);
        this.logger.info("Startup");

    
       
        

        registration.registerSupplier(supplier);

    }
    
}
