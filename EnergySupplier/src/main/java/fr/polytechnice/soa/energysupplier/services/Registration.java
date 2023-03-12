package fr.polytechnice.soa.energysupplier.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import fr.polytechnice.soa.energysupplier.interfaces.IRegistration;
import fr.polytechnice.soa.energysupplier.models.Supplier;
import fr.polytechnice.soa.energysupplier.repositories.SupplierRepository;

@Component
public class Registration implements IRegistration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SupplierRepository supplierRepository;

    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    private  final ObjectMapper objectMapper ;

    public Registration(Jackson2ObjectMapperBuilder objectMapperBuilder){
        this.objectMapper=objectMapperBuilder.build();
    }
    

    @Override
    public void registerSupplier(Supplier supplier) {
        this.saveIfNotExists(supplier);
        try {

            var json = objectMapper.writeValueAsString(supplier);
            rabbitTemplate.convertAndSend(topicExchangeName, "smartrix.supplier.registration", json);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saveIfNotExists(Supplier supplier){
        var optSup=this.supplierRepository.findByName(supplier.getName());

        if(optSup.isEmpty()){
            this.supplierRepository.save(supplier);
        }

    }



}
