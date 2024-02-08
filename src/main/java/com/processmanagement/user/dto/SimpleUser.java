package com.processmanagement.user.dto;

import com.processmanagement.domain.user.TeamMembershipType;
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
