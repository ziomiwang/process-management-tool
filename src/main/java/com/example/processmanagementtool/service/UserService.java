package com.example.processmanagementtool.service;

import com.example.processmanagementtool.repository.UserRepository;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<SuccessResponseDTO> saveUser(Mono<UserDTO> userDTO) {
        return userDTO.flatMap(this::valid)
                .flatMap(userRepository::saveUser)
                .map(ignore -> SuccessResponseDTO.builder()
                        .message("user successfully saved")
                        .build());
    }

    public Mono<UserDTO> getOneUser(Integer id) {
        return userRepository.getUser(id)
                .switchIfEmpty(Mono.empty());
    }

    public Flux<UserDTO> getALlUsers() {
        return userRepository.getAllUser();
    }

    public Flux<UserDTO> getAllUsersWithinAgeRange(Integer minAge, Integer maxAge) {
        return userRepository.getAllUsersWithFilteredAge(minAge, maxAge);
    }

    public Mono<SuccessResponseDTO> deleteUser(Integer id) {
        return userRepository.deleteUser(id)
                .map(ignore -> SuccessResponseDTO.builder()
                        .message("user successfully deleted")
                        .build());
    }

    public Mono<SuccessResponseDTO> updateUser(Integer id, Mono<UserDTO> userDTO){
        return userDTO.flatMap(u ->userRepository.updateUser(id, u))
                .map(ignore -> SuccessResponseDTO.builder()
                        .message("user successfully updated")
                        .build());
    }

    private Mono<UserDTO> valid(final UserDTO user) {

        if (user.getName().isBlank()) {
            return Mono.error(new RuntimeException("???"));
        }

        return Mono.just(user);
    }
}
