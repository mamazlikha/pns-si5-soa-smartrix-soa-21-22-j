package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component;

import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.AdjustmentRequest;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.ConsumptionEvolutionInRegion;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.entity.Supplier;
import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.repository.SupplierRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ContactEnergySupplierBean {

    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ContactReserveEnergyBean contactReserveEnergy;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = Logger.getLogger("[Orchestrator] " + ContactEnergySupplierBean.class.getName());

    public void manageConsumptionForRegion(ConsumptionEvolutionInRegion evolutionInRegion) {

        this.logger.info(evolutionInRegion.getEnergyT1() + " " + evolutionInRegion.getEnergyT2() + " "
                + evolutionInRegion.getRegion());
        List<Supplier> suppliers = supplierRepository.findByRegion(evolutionInRegion.getRegion());

        if (suppliers.isEmpty()) {
            logger.info("there is no supplier in this region 1");
        } else {

            int offset = evolutionInRegion.getEnergyT2() - evolutionInRegion.getEnergyT1();
            logger.info("Variation of energy " + offset);
            if (offset > 0) {
                boolean done = false;
                for (var supplier : suppliers) {

                    if (supplier.getCurrentProduction() + offset <= supplier.getMaximumCapacity()) {
                        var request = new AdjustmentRequest();
                        request.setProductionTarget(supplier.getCurrentProduction() + offset);
                        request.setSupplierName(supplier.getName());
                        try {
                            var requestStr = objectMapper.writeValueAsString(request);
                            this.rabbitTemplate.convertAndSend(topicExchangeName,
                                    "smartrix." + supplier.getName() + ".adjust-production", requestStr);

                            done = true;
                            break;
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (!done && suppliers.size() == 1) {//
                    logger.info("Pushing production of  " + suppliers.get(0).getName() + " to maximum capacity");
                    updateProduction(suppliers.get(0), offset);


                    contactReserveEnergy.getEnergyFromReserve(offset - (suppliers.get(0).getMaximumCapacity() - suppliers.get(0).getCurrentProduction()),evolutionInRegion.getRegion() );


                    return;
                }

                if (!done && suppliers.size() > 1) {

                    int totalMargin=suppliers.stream().mapToInt( s-> s.getMaximumCapacity()- s.getCurrentProduction() ).sum();


                    if(totalMargin < offset){
                        logger.info("Pushing production of everyone in region "+evolutionInRegion.getRegion()+" to maximum");

                        for(var supplier:suppliers){

                            this.updateProduction(supplier, offset);
                        }

                        contactReserveEnergy.getEnergyFromReserve(offset - totalMargin,evolutionInRegion.getRegion());

                    }
                    else{
                        for(var supplier:suppliers){
                            int supplierMargin=supplier.getMaximumCapacity()-supplier.getCurrentProduction();

                            double ratio=(double)supplierMargin/totalMargin;

                            this.updateProduction(supplier, (int)(offset*ratio));
                        }
                    }

                }
            }

        }

    }

    private void updateProduction(Supplier supplier, int offset){
        var request = new AdjustmentRequest();
        request.setProductionTarget(supplier.getCurrentProduction() + offset);
        request.setSupplierName(supplier.getName());

        try {
            var requestStr = objectMapper.writeValueAsString(request);
            this.rabbitTemplate.convertAndSend(topicExchangeName,
                    "smartrix." + supplier.getName() + ".adjust-production", requestStr);

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
