package com.smartrix2122soa2122j.smartrix.configurations;

import com.smartrix2122soa2122j.smartrix.components.RegisterBankDetails;

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
public class MessagingForBankAccountInformation {

    @Bean("customerDupExchange")
    @Qualifier("customerDupExchange")
    TopicExchange exchange() {

        return new TopicExchange("mail-exchange");
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
            @Qualifier("customerDupExchange") TopicExchange exchange) {
        return BindingBuilder.bind(anonymousQueueCustomer).to(exchange).with("*.accounting.info");
    }

    @Bean("customerDupListener")
    @Qualifier("customerDupListener")
    MessageListenerAdapter listenerAdapterCustomerDup(RegisterBankDetails registerBankDetails) {
        return new MessageListenerAdapter(registerBankDetails, "dupCustomer");
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
