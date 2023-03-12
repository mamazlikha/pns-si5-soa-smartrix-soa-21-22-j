package fr.polytechnice.soa.energysupplier.configurations;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.polytechnice.soa.energysupplier.services.RegistrationAck;

@Configuration
public class RegistrationConfig {

    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    

    @Value("${supplier.company.name}")
    private String supplierName;

    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }


   


    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }


    @Bean
    Binding binding(Queue autoDeleteQueue1, TopicExchange exchange) {
        return BindingBuilder.bind(autoDeleteQueue1).to(exchange).with(this.supplierName);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RegistrationAck receiver) {
        return new MessageListenerAdapter(receiver, "listenAck");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter, Queue autoDeleteQueue1) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(autoDeleteQueue1.getActualName());
        container.setMessageListener(listenerAdapter);
        return container;
    }
}
