package com.processmanagement.domain.prospect;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("prospect")
public class Prospect {

    @Id
    private Long id;
    private Map<String, ProspectProperty> fields;
    private String templateName;

    @Column("template_id")
    private Long templateId;

    @Column("user_id")
    private Long userId;
}
