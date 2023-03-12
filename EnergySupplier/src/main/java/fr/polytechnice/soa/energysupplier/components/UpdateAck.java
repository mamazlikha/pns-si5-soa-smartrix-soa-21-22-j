package fr.polytechnice.soa.energysupplier.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.polytechnice.soa.energysupplier.models.Supplier;

@Component
public class UpdateAck {
    
    @Value("${topic.exchange.supplier.name}")
    private String topicExchangeName;

    @Value("${queue.name}")
    private String queueName;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendUpdateToSmartrix(Supplier supplier){
        

        try {

            var json = objectMapper.writeValueAsString(supplier);
            rabbitTemplate.convertAndSend(topicExchangeName, "smartrix.supplier.update.ack", json);
        } catch (JsonProcessingException e) {
            
            e.printStackTrace();
        }
    }
}
