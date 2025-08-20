package ssu.cromi.teamit.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * List<String> ↔ JSON 문자열 변환기
 */
@Converter
public class StringListToJsonConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute); // List → JSON
        } catch (Exception e) {
            throw new RuntimeException("List를 JSON 문자열로 변환 실패", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(dbData, new TypeReference<>() {}); // JSON → List
        } catch (Exception e) {
            throw new RuntimeException("JSON 문자열을 List로 변환 실패", e);
        }
    }
}
