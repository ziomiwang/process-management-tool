package com.example.processmanagementtool.domain.prospect;

import com.example.processmanagementtool.dto.ProspectPropertyDTO;
import com.example.processmanagementtool.dto.TemplatePropertyDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProspectProperty {

    private Boolean required;
    private Object data;

    public static ProspectProperty map(ProspectPropertyDTO prospectPropertyDTO) {
        return ProspectProperty.builder()
                .required(prospectPropertyDTO.getRequired())
                .data(prospectPropertyDTO.getData())
                .build();
    }

    public static ProspectPropertyDTO map(ProspectProperty prospectProperty) {
        return ProspectPropertyDTO.builder()
                .required(prospectProperty.getRequired())
                .data(prospectProperty.getData())
                .build();
    }
}
