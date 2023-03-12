package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.configurations;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component.EnergyOrchestratorBean;

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

@Configuration
public class MessagingForGettingEnergyConsumption {

    @Value("${topic.exchange.monitor.name}")
    private String topicExchangeName;

    
    @Bean
    Queue anonymousQueueMonitorResponse() {
        return new AnonymousQueue();
    }

    @Bean("exchangeOrchestratorEnergy")
    @Qualifier("exchangeOrchestratorEnergy")
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }


    @Bean("bindingOrchestratorEnergy")
    Binding binding(Queue anonymousQueueMonitorResponse, @Qualifier("exchangeOrchestratorEnergy") TopicExchange exchange) {
        return BindingBuilder.bind(anonymousQueueMonitorResponse).to(exchange).with("smartrix.orchestrator.total.energy.consumed");
    }

    @Bean("listenerAdapterOrchestratorEnergy")
    @Qualifier("listenerAdapterOrchestratorEnergy")
    MessageListenerAdapter listenerAdapter(EnergyOrchestratorBean energyOrchestrator) {
        return new MessageListenerAdapter(energyOrchestrator, "receiveTotalEnergyConsumedOnFixeRate");
    }

    @Bean("containerOrchestratorEnergy")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
    @Qualifier("listenerAdapterOrchestratorEnergy") MessageListenerAdapter listenerAdapter,Queue anonymousQueueMonitorResponse) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(anonymousQueueMonitorResponse.getActualName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

}
