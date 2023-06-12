package com.example.processmanagementtool.user;

import com.example.processmanagementtool.domain.user.User;
import com.example.processmanagementtool.dto.AuthResponse;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.dto.UserResponseDTO;
import com.example.processmanagementtool.security.model.CustomUserDetails;
import com.example.processmanagementtool.utlitiy.JWTUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserDTOMapper {

    public static UserResponseDTO mapUserToResponse(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User mapRequestToUser(UserRequestDTO requestDTO) {
        return User.builder()
                .login(requestDTO.getLogin())
                .password(requestDTO.getPassword())
                .name(requestDTO.getName())
                .build();
    }
}
