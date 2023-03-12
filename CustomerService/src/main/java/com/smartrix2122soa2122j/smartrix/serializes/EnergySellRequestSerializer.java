package com.smartrix2122soa2122j.smartrix.serializes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.entity.EnergySellRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class EnergySellRequestSerializer implements Serializer<EnergySellRequest> {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, EnergySellRequest data) {
        try {
            if (data == null){
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageDto to byte[]");
        }
    }

    @Override
    public void close() {
    }
}