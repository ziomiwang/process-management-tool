package com.example.processmanagementtool.domain.template;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table("template")
public class Template {

    @Id
    private Long id;
    private String name;

    private Map<String, TemplateProperty> fields;

    @Column("user_id")
    private Long userId;

}
