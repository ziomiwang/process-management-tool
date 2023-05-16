package com.example.processmanagementtool.service;

import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserDTO;
import com.example.processmanagementtool.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final UserDTO testUser = UserDTO.builder()
            .id(1)
            .age(22)
            .name("Michal")
            .password("passwd")
            .build();

    @Test
    void shouldSaveUser() {
        Mockito.when(userRepository.saveUser(testUser)).thenReturn(Mono.just(testUser));
        Mono<SuccessResponseDTO> saveOperationResponse = userService.saveUser(Mono.just(testUser));

        StepVerifier
                .create(saveOperationResponse)
                .consumeNextWith(res -> assertEquals(res.getMessage(), "user successfully saved"))
                .verifyComplete();
    }

    @Test
    void shouldGetOneUser() {

        Mockito.when(userRepository.getUser(0)).thenReturn(Mono.just(testUser));
        Mono<UserDTO> userMono = userService.getOneUser(0);

        StepVerifier
                .create(userMono)
                .consumeNextWith(user -> assertEquals(user.getName(), testUser.getName()))
                .verifyComplete();
    }

    @Test
    void shouldGetAllUsers() {
        Mockito.when(userRepository.getAllUser()).thenReturn(Flux.just(testUser));
        Flux<UserDTO> userFlux = userService.getALlUsers();

        StepVerifier
                .create(userFlux)
                .consumeNextWith(user -> assertEquals(user.getName(), testUser.getName()))
                .verifyComplete();
    }

    @Test
    void shouldDeleteUser() {
        Mockito.when(userRepository.deleteUser(0)).thenReturn(Mono.just(testUser));
        Mono<SuccessResponseDTO> deleteOperationResponse = userService.deleteUser(0);

        StepVerifier
                .create(deleteOperationResponse)
                .consumeNextWith(res -> assertEquals(res.getMessage(), "user successfully deleted"))
                .verifyComplete();
    }

    @Test
    void shouldUpdateUser() {
        Mockito.when(userRepository.updateUser(0, testUser)).thenReturn(Mono.just(testUser));
        Mono<SuccessResponseDTO> updateOperationResponse = userService.updateUser(0, Mono.just(testUser));

        StepVerifier
                .create(updateOperationResponse)
                .consumeNextWith(res -> assertEquals(res.getMessage(), "user successfully updated"))
                .verifyComplete();
    }
}