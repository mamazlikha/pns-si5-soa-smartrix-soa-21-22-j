package fr.polytechnice.soa.energysupplier.services;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import fr.polytechnice.soa.energysupplier.components.UpdateAck;
import fr.polytechnice.soa.energysupplier.interfaces.ISupply;
import fr.polytechnice.soa.energysupplier.models.AdjustmentRequest;
import fr.polytechnice.soa.energysupplier.models.EnergySuppliedRequest;
import fr.polytechnice.soa.energysupplier.models.Supplier;
import fr.polytechnice.soa.energysupplier.repositories.SupplierRepository;

@Service
public class SupplyService implements ISupply {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UpdateAck updateAck;

    @Value("${supplier.company.name}")
    private String supplierName;

    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Value("${queue.supply.energy.name}")
    private String queueName;

    private Logger logger=Logger.getLogger(SupplyService.class.getName());
    private  final ObjectMapper objectMapper ;

    public SupplyService(Jackson2ObjectMapperBuilder objectMapperBuilder){
        this.objectMapper=objectMapperBuilder.build();
    }


    @Override
    public void getEnergyCurrentlySupplied(EnergySuppliedRequest energySuppliedRequest) {
        if(!energySuppliedRequest.getName().equals(supplierName)){
            return;
        }

       
        var optSup= this.supplierRepository.findByName(supplierName);

        if(optSup.isPresent()){
            try {
                Supplier supplier = optSup.get();
                var json = objectMapper.writeValueAsString(supplier.getCurrentProduction());
                rabbitTemplate.convertAndSend(topicExchangeName, "smartrix.supplier.client-production", json);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getProductionCapacity(EnergySuppliedRequest energySuppliedRequest) {
        if(!energySuppliedRequest.getName().equals(supplierName)){
            return;
        }
        
        var optSup= this.supplierRepository.findByName(supplierName);

        if(optSup.isPresent()){
            try {
                Supplier supplier = optSup.get();
                var json = objectMapper.writeValueAsString(supplier.getMaximumCapacity());
                rabbitTemplate.convertAndSend(topicExchangeName, "smartrix.supplier.capacity-production", json);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void adjustEnergySupplied(AdjustmentRequest request) {

        var optSup = supplierRepository.findByName(supplierName);

        if (optSup.isPresent()) {
            this.logger.info(this.supplierName+" handling AdjustmentRequest message ");


            var supplier = optSup.get();
            var nextProduction = request.productionTarget();

            nextProduction = nextProduction > supplier.getMaximumCapacity() ? supplier.getMaximumCapacity()
                    : nextProduction;
            nextProduction = nextProduction < 0 ? 0 : nextProduction;

            this.logger.info(this.supplierName+ " changes production from "+supplier.getCurrentProduction()+" to "+nextProduction);
            supplier.setCurrentProduction(nextProduction);

            supplier=supplierRepository.save(supplier);

            this.logger.info(this.supplierName +" confirms message reception & changes to Smartrix Grid");
            this.updateAck.sendUpdateToSmartrix(supplier);
        }

    }


}
