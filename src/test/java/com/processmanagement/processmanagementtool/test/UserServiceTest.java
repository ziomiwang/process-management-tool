package com.processmanagement.processmanagementtool.test;

import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.dto.UserResponseDTO;
import com.processmanagement.domain.user.User;
import com.processmanagement.domain.user.repository.UserRepository;
import com.processmanagement.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final User testUser = User.builder()
            .login("test")
            .password("test")
            .name("test")
            .build();

    private final UserRequestDTO testRequest = UserRequestDTO
            .builder()
            .login("test")
            .password("test")
            .name("test")
            .build();

    @Test
    void shouldGetOneUser() {

        Mockito.when(userRepository.findById(0L)).thenReturn(Mono.just(testUser));
        Mono<UserResponseDTO> userMono = userService.getOneUser(0L);

        StepVerifier
                .create(userMono)
                .consumeNextWith(user -> assertEquals(user.getName(), testUser.getName()))
                .verifyComplete();
    }

    @Test
    void shouldGetAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(Flux.just(testUser));
        Flux<UserResponseDTO> userFlux = userService.getALlUsers();

        StepVerifier
                .create(userFlux)
                .consumeNextWith(user -> assertEquals(user.getName(), testUser.getName()))
                .verifyComplete();
    }

    @Test
    void shouldDeleteUser() {
        Mockito.doNothing().when(userRepository.deleteById(0L));
        Mono<SuccessResponseDTO> deleteOperationResponse = userService.deleteUser(0L);

        StepVerifier
                .create(deleteOperationResponse)
                .consumeNextWith(res -> assertEquals(res.getMessage(), "user successfully deleted"))
                .verifyComplete();
    }
}