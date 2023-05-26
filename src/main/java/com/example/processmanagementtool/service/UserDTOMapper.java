package com.example.processmanagementtool.service;

import com.example.processmanagementtool.domain.user.User;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    public static UserResponseDTO mapUserToResponse(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User mapRequestToUser(UserRequestDTO requestDTO){
        return User.builder()
                .login(requestDTO.getLogin())
                .password(requestDTO.getPassword())
                .name(requestDTO.getName())
                .build();
    }
}
