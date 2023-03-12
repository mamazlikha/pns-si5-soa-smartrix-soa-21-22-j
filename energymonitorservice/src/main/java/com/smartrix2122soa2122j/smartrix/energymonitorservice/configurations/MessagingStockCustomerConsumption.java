package com.smartrix2122soa2122j.smartrix.energymonitorservice.configurations;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.Measure;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
public class MessagingStockCustomerConsumption {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public ConsumerFactory<String, String> consumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "20971520");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "20971520");
        return new DefaultKafkaConsumerFactory<>(props);
    }


    public ConsumerFactory<String, Measure> energyConsumptionRequestConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupidsavecustomerconsumption");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Measure.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Measure> energyConsumptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Measure> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(energyConsumptionRequestConsumerFactory());
        return factory;
    }

}
