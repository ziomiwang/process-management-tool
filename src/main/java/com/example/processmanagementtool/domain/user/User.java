package com.example.processmanagementtool.domain.user;

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
@Table("_user")
public class User {

    @Id
    private Long id;
    private String login;
    private String password;
    private String name;
    private Role role;
    private TeamMembershipType membershipType;

    @Column("team_id")
    private Long teamId;
}
