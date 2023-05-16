package com.example.processmanagementtool.repository;

import com.example.processmanagementtool.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepository();

    private final UserDTO testUser = UserDTO.builder()
            .id(1)
            .age(22)
            .name("Michal")
            .password("passwd")
            .build();

    @Test
    void saveUser() {
        Mono<UserDTO> savedUser = userRepository.saveUser(testUser);

        StepVerifier
                .create(savedUser)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getUser() {
        Mono<UserDTO> userFromDb = userRepository.getUser(1);
        StepVerifier
                .create(userFromDb)
                .consumeNextWith(userDb -> assertEquals(userDb.getName(), testUser.getName()))
                .verifyComplete();
    }

    @BeforeEach
    void initDB() {
        userRepository.saveUser(testUser)
                .block();
    }

    @Test
    void getAllUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }
}