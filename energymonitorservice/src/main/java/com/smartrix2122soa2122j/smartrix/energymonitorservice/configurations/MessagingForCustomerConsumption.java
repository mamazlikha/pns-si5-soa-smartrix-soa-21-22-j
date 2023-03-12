package com.smartrix2122soa2122j.smartrix.energymonitorservice.configurations;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.controller.CustomerInterface;

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
public class MessagingForCustomerConsumption {
    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Value("${queue.customer.monitor.name}")
    private String queueName;


    @Bean("exchangeEnergyConsumed")
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean("queueAskingMeasure")
    @Qualifier("queueAskingMeasure")
    Queue queue() {
        return new Queue(queueName, false);
    }


    @Bean("bindingAskingMeasure")
    Binding binding(@Qualifier("queueAskingMeasure")  Queue queue, @Qualifier("exchangeEnergyConsumed") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("smartrix.measure.asking-measure");
    }

    @Bean("listenerHandlingConsumerResquest")
    MessageListenerAdapter listenerAdapter(CustomerInterface customerInterface) {
        return new MessageListenerAdapter(customerInterface, "computeConsumationForConsumer");
    }

    @Bean("handlingConsumerRequest")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             @Qualifier ("listenerHandlingConsumerResquest") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

}
