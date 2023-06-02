package com.example.processmanagementtool.user;

import com.example.processmanagementtool.domain.user.User;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.dto.UserResponseDTO;
import com.example.processmanagementtool.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<SuccessResponseDTO> saveUser(Mono<UserRequestDTO> userDTO) {
        return userDTO.flatMap(this::valid)
                .flatMap(userRepository::save)
                .map(ignore -> SuccessResponseDTO.builder()
                        .message("saved user with login " + ignore.getLogin())
                        .build());
    }

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
//        return userDTO.flatMap(data -> userRepository.findById(id)
//                        .map(foundUser -> updateUserName(data, foundUser)))
//                .map(ignore -> SuccessResponseDTO.builder()
//                        .message("user successfully updated")
//                        .build());
        return mapUserToResponse(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")));
    }

    private Mono<SuccessResponseDTO> mapUserToResponse(Long id){
        return userRepository.findById(id)
                .flatMap(ignored -> Mono.just(SuccessResponseDTO.builder().build()));
    }

    private Mono<User> updateUserName(UserRequestDTO editUserData, User foundUser) {
        foundUser.setName(editUserData.getName());
        return Mono.just(foundUser);
    }

    private Mono<User> valid(final UserRequestDTO user) {

        if (user.getLogin().isBlank()) {
            return Mono.error(new RuntimeException("???"));
        }

        System.out.println("USER DATA: " + user);

        return Mono.just(UserDTOMapper.mapRequestToUser(user));
    }
}
