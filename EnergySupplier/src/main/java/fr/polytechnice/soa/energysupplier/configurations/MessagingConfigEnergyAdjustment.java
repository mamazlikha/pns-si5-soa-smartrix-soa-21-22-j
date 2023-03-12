package fr.polytechnice.soa.energysupplier.configurations;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.polytechnice.soa.energysupplier.controllers.SupplyController;

@Configuration
public class MessagingConfigEnergyAdjustment {
    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Value("${supplier.company.name}")
    private String supplierName;

 
  

    @Bean
    public Queue adjustEnergyQueue() {
        return new AnonymousQueue();
    }


    @Bean("bindingForAdjust")
    Binding binding(  Queue adjustEnergyQueue, TopicExchange exchange) {
        return BindingBuilder.bind(adjustEnergyQueue).to(exchange).with("smartrix."+ supplierName+ ".adjust-production");
    }

    @Bean("listenerAdapterForAdjust")
    MessageListenerAdapter listenerAdapter(SupplyController supplyController) {
        return new MessageListenerAdapter(supplyController, "adjustEnergyProduced");
    }

    @Bean("containerForAdjust")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
    @Qualifier ("listenerAdapterForAdjust") MessageListenerAdapter listenerAdapter,Queue adjustEnergyQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(adjustEnergyQueue.getActualName());
        container.setMessageListener(listenerAdapter);
        return container;
    }
}
