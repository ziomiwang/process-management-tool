package com.processmanagement.domain.team;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table("team")
public class Team {

    @Id
    private Long id;
    @Column("owner_user_id")
    private Long ownerId;

}
