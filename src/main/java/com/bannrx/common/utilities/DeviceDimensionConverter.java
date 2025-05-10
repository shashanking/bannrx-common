package com.bannrx.common.utilities;

import com.bannrx.common.dtos.device.DimensionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public class DeviceDimensionConverter implements AttributeConverter<DimensionDto, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(DimensionDto attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to convert DeviceDimension to JSON", e);
        }
    }

    @Override
    public DimensionDto convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : objectMapper.readValue(dbData, DimensionDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to convert JSON to DeviceDimension", e);
        }
    }
}
