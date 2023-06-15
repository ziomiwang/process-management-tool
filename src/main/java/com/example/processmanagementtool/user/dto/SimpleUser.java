package com.example.processmanagementtool.user.dto;

import com.example.processmanagementtool.domain.user.TeamMembershipType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleUser {

    private String login;
    private String name;
    private TeamMembershipType membershipType;
}
