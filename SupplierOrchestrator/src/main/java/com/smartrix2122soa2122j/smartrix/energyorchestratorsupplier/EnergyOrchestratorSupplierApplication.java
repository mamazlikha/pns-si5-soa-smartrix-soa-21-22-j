package com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier;

import com.smartrix2122soa2122j.smartrix.energyorchestratorsupplier.component.SupplierRegistration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EnergyOrchestratorSupplierApplication {

	@Value("${topic.exchange.name}")
	private String topicExchangeName;

	@Value("${queue.name}")
	private String queueName;

	@Bean("queueSaveSuppliers")
	@Qualifier("queueSaveSuppliers")
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean("exchangeSaveSuppliers")
	@Qualifier("exchangeSaveSuppliers")
	TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	@Bean("bindingSaveSuppliers")
	Binding binding(@Qualifier("queueSaveSuppliers") Queue queue, @Qualifier("exchangeSaveSuppliers") TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("smartrix.supplier.registration");
	}

	@Bean("ListenerAdapterSaveSuppliers")
	@Qualifier("ListenerAdapterSaveSuppliers")
	MessageListenerAdapter listenerAdapter(SupplierRegistration receiver) {
		return new MessageListenerAdapter(receiver, "registerSupplier");
	}


	@Bean("containerSaveSuppliers")
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 @Qualifier("ListenerAdapterSaveSuppliers") MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.run(EnergyOrchestratorSupplierApplication.class, args);
	}

}
