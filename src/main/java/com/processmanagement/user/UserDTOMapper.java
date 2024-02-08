package com.processmanagement.user;

import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.dto.UserResponseDTO;
import com.processmanagement.domain.user.User;
import com.processmanagement.user.dto.SimpleUser;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOMapper {

    public static UserResponseDTO mapUserToResponse(User user) {
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

    public static SimpleUser mapUserToSimpleUser(User user) {
        return SimpleUser.builder()
                .login(user.getLogin())
                .name(user.getName())
                .membershipType(user.getMembershipType())
                .build();
    }

    public static List<SimpleUser> mapUserToSimpleUser(List<User> users) {
        return users.stream()
                .map(UserDTOMapper::mapUserToSimpleUser)
                .collect(Collectors.toList());
    }
}
