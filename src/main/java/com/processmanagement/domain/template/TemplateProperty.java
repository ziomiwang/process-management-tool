package com.processmanagement.domain.template;

import com.example.processmanagementtool.dto.TemplatePropertyDTO;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateProperty {

    private Boolean required;
    private Object data;

    public static TemplateProperty map(TemplatePropertyDTO templateProperty) {
        return TemplateProperty.builder()
                .required(templateProperty.getRequired())
                .data(templateProperty.getData())
                .build();
    }

    public static TemplatePropertyDTO map(TemplateProperty templateProperty) {
        return TemplatePropertyDTO.builder()
                .required(templateProperty.getRequired())
                .data(templateProperty.getData())
                .build();
    }
}
