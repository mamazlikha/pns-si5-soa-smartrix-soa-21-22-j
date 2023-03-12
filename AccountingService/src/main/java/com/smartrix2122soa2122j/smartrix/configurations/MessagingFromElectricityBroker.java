package com.smartrix2122soa2122j.smartrix.configurations;

import com.smartrix2122soa2122j.smartrix.components.ElectricityBrokerInterface;
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
public class MessagingFromElectricityBroker {
    @Value("${topic.exchange.reserve.name}")
    private String topicExchangeName;

    @Value("${queue.customer.electricity-broke.name}")
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
        return BindingBuilder.bind(queue).to(exchange).with("smartrix-accounting-energy-sold-messaging");
    }

    @Bean("listenerHandlingConsumerResquest")
    MessageListenerAdapter listenerAdapter(ElectricityBrokerInterface electricityBrokerInterface) {
        return new MessageListenerAdapter(electricityBrokerInterface, "creditCustomer");
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
