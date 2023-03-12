package fr.polytechnice.soa.energysupplier.configurations;

import fr.polytechnice.soa.energysupplier.controllers.SupplyController;
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
public class MessagingConfigForGettingProductionCapacity {

    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Value("${queue.supply.energy.name}")
    private String queueName;

    @Bean("queueForProduction")
    @Qualifier("queueProduction")
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean("exchangeProduction")
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }


    @Bean("bindingProduction")
    Binding binding(@Qualifier("queueProduction")  Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("smartrix.supplier.capacity-production");
    }

    @Bean("listenerAdapterProduction")
    MessageListenerAdapter listenerAdapter(SupplyController supplyController) {
        return new MessageListenerAdapter(supplyController, "getProductionCapacity");
    }

    @Bean("containerProduction")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }


}
