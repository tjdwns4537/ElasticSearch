package com.example.elasticsearch.stock.domain;

import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class StockElasticDtoDeserializer implements Deserializer<StockElasticDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration required
    }

    @Override
    public StockElasticDto deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, StockElasticDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize StockElasticDto", e);
        }
    }

    @Override
    public void close() {
        // No resources to release
    }
}
