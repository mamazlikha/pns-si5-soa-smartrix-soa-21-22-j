package com.smartrixgridj.emailservice.configurations;

import java.util.logging.Logger;

import com.smartrixgridj.emailservice.services.EmailService;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfigurations {


    private Logger logger=Logger.getLogger(MessagingConfigurations.class.getName());

    @Bean
    Queue anonymousQueueEmail() {
        return new AnonymousQueue();
    }

    @Bean
    TopicExchange exchange() {
        
        return new TopicExchange("mail-exchange");
    }

    @Bean
    Binding binding(Queue anonymousQueueEmail, TopicExchange exchange) {
        this.logger.info("creating exchange "+exchange +" "+anonymousQueueEmail);
        return BindingBuilder.bind(anonymousQueueEmail).to(exchange).with("send.email.region");
    }

    @Bean
    MessageListenerAdapter listenerAdapter(EmailService emailService) {
        return new MessageListenerAdapter(emailService, "sendEmail");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter, Queue anonymousQueueEmail) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(anonymousQueueEmail.getActualName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    /** Duplicate of Customer */
    @Bean("customerDuplicationQueue")
    @Qualifier("customerDuplicationQueue")
    Queue anonymousQueueCustomer() {
        return new AnonymousQueue();
    }

    @Bean("customerBinding")
    @Qualifier("customerBinding")
    Binding bindingCustomerDuplication(@Qualifier("customerDuplicationQueue") Queue anonymousQueueCustomer,
            TopicExchange exchange) {
        return BindingBuilder.bind(anonymousQueueCustomer).to(exchange).with("mail.*.info");
    }

    @Bean("customerDupListener")
    @Qualifier("customerDupListener")
    MessageListenerAdapter listenerAdapterCustomerDup(EmailService emailService) {
        return new MessageListenerAdapter(emailService, "dupCustomer");
    }

    @Bean("customerDupContainer")
    @Qualifier("customerDupContainer")
    SimpleMessageListenerContainer containerDupContainer(ConnectionFactory connectionFactory,
            @Qualifier("customerDupListener") MessageListenerAdapter listenerAdapterCustomerDup,
            @Qualifier("customerDuplicationQueue") Queue anonymousQueueCustomer) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(anonymousQueueCustomer.getActualName());
        container.setMessageListener(listenerAdapterCustomerDup);
        return container;
    }

}
