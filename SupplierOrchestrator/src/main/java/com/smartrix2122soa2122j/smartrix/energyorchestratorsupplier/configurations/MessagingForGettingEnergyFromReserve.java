package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.configurations;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component.ContactReserveEnergyBean;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingForGettingEnergyFromReserve {

    @Value("${topic.exchange.reserve.name}")
    private String topicExchangeName;


    @Value("${queue.energy.reserve.name}")
    private String queueName;

    @Bean("queueUpdateReserve")
    @Qualifier("queueUpdateReserve")
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean("exchangeUpdateReserve")
    @Qualifier("exchangeUpdateReserve")
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }


    @Bean("bindingUpdateReserve")
    Binding binding(@Qualifier("queueUpdateReserve")  Queue queue, @Qualifier("exchangeUpdateReserve") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("smartrix.reserve.get.energy");
    }

    @Bean("listenerAdapterUpdateReserve")
    @Qualifier("listenerAdapterUpdateReserve")
    MessageListenerAdapter listenerAdapter(ContactReserveEnergyBean contactReserveEnergy) {
        return new MessageListenerAdapter(contactReserveEnergy, "updateEnergyConsumption");
    }

    @Bean("containerUpdateReserve")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             @Qualifier("listenerAdapterUpdateReserve") MessageListenerAdapter listenerAdapter, Queue anonymousQueueMonitorResponse) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(anonymousQueueMonitorResponse.getActualName());
        container.setMessageListener(listenerAdapter);
        return container;
    }



}
