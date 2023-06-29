package com.example.processmanagementtool.prospect;

import com.example.processmanagementtool.domain.prospect.Prospect;
import com.example.processmanagementtool.domain.prospect.ProspectProperty;
import com.example.processmanagementtool.dto.ProspectPropertyDTO;
import com.example.processmanagementtool.dto.ProspectRequestDTO;
import com.example.processmanagementtool.dto.ProspectResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProspectDTOMapper {

    public static Prospect mapDTOtoProspect(ProspectRequestDTO prospectRequestDTO) {
        return Prospect.builder()
                .templateId(prospectRequestDTO.getTemplateId())
                .templateName(prospectRequestDTO.getTemplateName())
                .fields(mapDTOFieldsToProspectFields(prospectRequestDTO.getFields()))
                .build();
    }

    public static ProspectProperty mapProspectPropertyDTOtoProspectProperty(ProspectPropertyDTO prospectPropertyDTO) {
        return ProspectProperty.builder()
                .data(prospectPropertyDTO.getData())
                .build();
    }

    public static ProspectResponseDTO mapProspectToResponse(Prospect prospect) {
        return ProspectResponseDTO.builder()
                .templateId(prospect.getTemplateId())
                .fields(mapFieldsToFieldsDto(prospect.getFields()))
                .build();
    }

    public static List<ProspectResponseDTO> mapProspectToResponse(List<Prospect> prospects) {
        return prospects.stream()
                .map(ProspectDTOMapper::mapProspectToResponse)
                .collect(Collectors.toList());
    }

    private static Map<String, ProspectPropertyDTO> mapFieldsToFieldsDto(Map<String, ProspectProperty> fields) {
        return fields.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> ProspectProperty.map(e.getValue())));
    }

    private static Map<String, ProspectProperty> mapDTOFieldsToProspectFields(Map<String, ProspectPropertyDTO> fields) {
        return fields.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> ProspectProperty.map(e.getValue())));
    }
}
