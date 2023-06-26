package com.example.processmanagementtool.configuration;

import com.example.processmanagementtool.utlitiy.JsonToTemplateMapConverter;
import com.example.processmanagementtool.utlitiy.TemplateMapToJsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ReactivePostgresConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    @Primary
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new JsonToTemplateMapConverter(objectMapper));
        converters.add(new TemplateMapToJsonConverter(objectMapper));
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }
}
