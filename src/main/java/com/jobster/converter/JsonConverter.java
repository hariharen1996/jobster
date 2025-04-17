package com.jobster.converter;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonConverter implements AttributeConverter<Map<String, List<String>>, String> {

    private final ObjectMapper objectMapper;

    public JsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(Map<String, List<String>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error converting to json", e);
        }
    }

    @Override
    public Map<String, List<String>> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<Map<String, List<String>>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error converting json to map", e);
        }
    }

}
