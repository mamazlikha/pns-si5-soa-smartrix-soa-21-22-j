package com.smartrix2122soa2122j.smartrix.reserveenergy.configurations;

import com.smartrix2122soa2122j.smartrix.reserveenergy.components.ConsumeEnergy;
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
public class MessagingForGettingEnergy {


    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Value("${queue.energy.consume.name}")
    private String queueName;

    @Bean("queueConsumeEnergyReserve")
    @Qualifier("queueConsumeEnergyReserve")
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean("exchangeConsumeEnergyReserve")
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean("bindingConsumeEnergyReserve")
    Binding binding(@Qualifier("queueConsumeEnergyReserve")  Queue queue, @Qualifier("exchangeConsumeEnergyReserve") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("smartrix-get-energy-from-reserve");
    }

    @Bean("listenerAdapterConsumeEnergyReserve")
    MessageListenerAdapter listenerAdapter(ConsumeEnergy consumeEnergy) {
        return new MessageListenerAdapter(consumeEnergy, "sendEnergy");
    }

    @Bean("containerConsumeEnergyReserve")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             @Qualifier ("listenerAdapterConsumeEnergyReserve") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

}
