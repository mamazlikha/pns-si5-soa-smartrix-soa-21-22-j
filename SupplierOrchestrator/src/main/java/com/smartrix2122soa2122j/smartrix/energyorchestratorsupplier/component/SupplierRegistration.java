package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component;

import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.Supplier;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.SupplierRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SupplierRegistration {
    
    @Value("${topic.exchange.name}")
	private String topicExchangeName ;


    @Autowired 
    private SupplierRepository supplierRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final Logger logger=Logger.getLogger(SupplierRegistration.class.getName());

    public void registerSupplier(String supplierStr){
        this.logger.info("Receive supplier to register " + supplierStr);
        ObjectMapper mapper=new ObjectMapper();
        try {
            var supplier= mapper.readValue(supplierStr, Supplier.class);

            var optSup= supplierRepository.findByName(supplier.getName());

            if(optSup.isEmpty()){
                
                supplier=supplierRepository.save(supplier);
            }
            else{
                supplier=optSup.get();
            }

            rabbitTemplate.convertAndSend(topicExchangeName, supplier.getName(), mapper.writeValueAsString(supplier));
            


        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
