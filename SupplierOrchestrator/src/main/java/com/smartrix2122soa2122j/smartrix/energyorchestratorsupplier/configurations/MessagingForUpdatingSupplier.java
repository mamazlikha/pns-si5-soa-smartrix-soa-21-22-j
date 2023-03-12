package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.configurations;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component.EnergyOrchestratorBean;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingForUpdatingSupplier {

    @Value("${topic.exchange.supplier.name}")
    private String topicExchangeName;


    @Value("${queue.energy.reserve.name}")
    private String queueName;

    @Bean("queueUpdateSupplier")
    @Qualifier("queueUpdateSupplier")
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean("exchangeUpdateSupplier")
    @Qualifier("exchangeUpdateSupplier")
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }


    @Bean("bindingUpdateSupplier")
    Binding binding(@Qualifier("queueUpdateSupplier")  Queue queue, @Qualifier("exchangeUpdateSupplier") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("smartrix.supplier.update.ack");
    }

    @Bean("listenerAdapterUpdateSupplier")
    @Qualifier("listenerAdapterUpdateSupplier")
    MessageListenerAdapter listenerAdapter(EnergyOrchestratorBean energyOrchestrator) {
        return new MessageListenerAdapter(energyOrchestrator, "updateSupplier");
    }

    @Bean("containerUpdateSupplier")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             @Qualifier("listenerAdapterUpdateSupplier") MessageListenerAdapter listenerAdapter, Queue anonymousQueueMonitorResponse) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(anonymousQueueMonitorResponse.getActualName());
        container.setMessageListener(listenerAdapter);
        return container;
    }


}
