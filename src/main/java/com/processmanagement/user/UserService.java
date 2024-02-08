package com.processmanagement.user;

import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.dto.UserResponseDTO;
import com.processmanagement.domain.user.User;
import com.processmanagement.domain.user.repository.UserRepository;
import com.processmanagement.exception.customexceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<UserResponseDTO> getOneUser(Long id) {
        return userRepository.findById(id)
                .map(UserDTOMapper::mapUserToResponse)
                .switchIfEmpty(Mono.empty());
    }

    public Flux<UserResponseDTO> getALlUsers() {
        return userRepository.findAll()
                .map(UserDTOMapper::mapUserToResponse);
    }

    public Mono<SuccessResponseDTO> deleteUser(Long id) {
        return userRepository.deleteById(id)
                .map(ignore -> SuccessResponseDTO.builder()
                        .message("user successfully deleted")
                        .build());
    }

    public Mono<SuccessResponseDTO> updateUser(Long id, Mono<UserRequestDTO> userDTO) {
        return mapUserToResponse(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")));
    }

    private Mono<SuccessResponseDTO> mapUserToResponse(Long id) {
        return userRepository.findById(id)
                .flatMap(ignored -> Mono.just(SuccessResponseDTO.builder().build()));
    }

    private Mono<User> updateUserName(UserRequestDTO editUserData, User foundUser) {
        foundUser.setName(editUserData.getName());
        return Mono.just(foundUser);
    }
}
