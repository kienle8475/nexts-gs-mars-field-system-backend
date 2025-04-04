package com.nexts.gs.mars.nexts_gs_mars_field_service.Converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;
import java.util.List;
import java.util.Collections;

@Converter(autoApply = false)
public class JsonListConverter implements AttributeConverter<List<Map<String, Object>>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<Map<String, Object>> attribute) {
    try {
      return attribute == null ? null : objectMapper.writeValueAsString(attribute);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error converting List<Map> to JSON string", e);
    }
  }

  @Override
  public List<Map<String, Object>> convertToEntityAttribute(String dbData) {
    try {
      return dbData == null
          ? Collections.emptyList()
          : objectMapper.readValue(dbData, new TypeReference<>() {
          });
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }
}