package com.example.processmanagementtool.domain.prospect;

import com.example.processmanagementtool.dto.ProspectPropertyDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProspectProperty {

    private Object data;

    public static ProspectProperty map(ProspectPropertyDTO prospectPropertyDTO) {
        return ProspectProperty.builder()
                .data(prospectPropertyDTO.getData())
                .build();
    }

    public static ProspectPropertyDTO map(ProspectProperty prospectProperty) {
        return ProspectPropertyDTO.builder()
                .data(prospectProperty.getData())
                .build();
    }
}
