package com.processmanagement.utlitiy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.processmanagement.domain.template.TemplateProperty;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.Map;

@Slf4j
@WritingConverter
@RequiredArgsConstructor
public class TemplateMapToJsonConverter implements Converter<Map<String, TemplateProperty>, Json> {
    private final ObjectMapper objectMapper;

    @Override
    public Json convert(Map<String, TemplateProperty> source) {
        try {
            return Json.of(objectMapper.writeValueAsString(source));
        } catch (JsonProcessingException e) {
            log.error("Error occurred while serializing map to JSON: {}", source, e);
        }
        return Json.of("");
    }
}
