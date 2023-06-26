package com.example.processmanagementtool.template;

import com.example.processmanagementtool.domain.template.Template;
import com.example.processmanagementtool.domain.template.TemplateProperty;
import com.example.processmanagementtool.dto.TemplatePropertyDTO;
import com.example.processmanagementtool.dto.TemplateRequestDTO;
import com.example.processmanagementtool.dto.TemplateResponseDTO;

import java.util.Map;
import java.util.stream.Collectors;

public class TemplateDTOMapper {

    public static TemplateResponseDTO mapTemplateToTemplateResponseDTO(Template template) {
        return TemplateResponseDTO.builder()
                .name(template.getName())
                .fields(mapFieldsToFieldsDto(template.getFields()))
                .build();
    }

    private static Map<String, TemplatePropertyDTO> mapFieldsToFieldsDto(Map<String, TemplateProperty> fields) {
        return fields.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> TemplateProperty.map(e.getValue())));
    }

    public static Template mapTemplateRequestDTOToTemplate(TemplateRequestDTO templateRequestDTO) {
        return Template.builder()
                .name(templateRequestDTO.getName())
                .fields(mapDTOFieldsToTemplateFields(templateRequestDTO.getFields()))
                .build();
    }

    private static Map<String, TemplateProperty> mapDTOFieldsToTemplateFields(Map<String, TemplatePropertyDTO> fields) {
        return fields.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> TemplateProperty.map(e.getValue())));
    }
}
