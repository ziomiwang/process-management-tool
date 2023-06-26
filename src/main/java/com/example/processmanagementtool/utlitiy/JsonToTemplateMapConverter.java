package com.example.processmanagementtool.utlitiy;

import com.example.processmanagementtool.domain.template.TemplateProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ReadingConverter
@RequiredArgsConstructor
public class JsonToTemplateMapConverter implements Converter<Json, Map<String, TemplateProperty>> {

    private final ObjectMapper objectMapper;

    @Override
    public Map<String,TemplateProperty> convert(Json source) {
        try {
            return objectMapper.readValue(source.asString(), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Problem while parsing JSON: {}", source, e);
        }
        return new HashMap<>();
    }
}
