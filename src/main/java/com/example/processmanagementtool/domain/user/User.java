package com.example.processmanagementtool.domain.user;

import com.example.processmanagementtool.dto.UserRequestDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("_user")
public class User {

    @Id
    private Long id;
    private String login;
    private String password;
    private String name;

    public static User map(UserRequestDTO userDTO){
        return User.builder()
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .build();
    }
}
