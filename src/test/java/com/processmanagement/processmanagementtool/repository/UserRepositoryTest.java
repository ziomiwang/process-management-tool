package com.processmanagement.processmanagementtool.repository;

import com.processmanagement.processmanagementtool.domain.user.User;
import com.processmanagement.processmanagementtool.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveSingleUserToDatabase() {
        User user = User.builder()
                .login("test1234678")
                .password("test")
                .name("testName")
                .build();

        Mono<User> savedUser = userRepository.save(user);

        StepVerifier
                .create(savedUser)
                .expectNextMatches(usr -> usr.getLogin().equals(user.getLogin()))
                .verifyComplete();
    }

    @Test
    void shouldSaveAndRetrieveUser(){
        User user = User.builder()
                .login("test12345")
                .password("test")
                .name("testName")
                .build();

        Mono<User> savedUser = userRepository.save(user);
        Mono<User> userFromDb = userRepository.findUserByLogin(user.getLogin());

        StepVerifier
                .create(savedUser)
                .expectNextCount(1)
                .verifyComplete();
        StepVerifier
                .create(userFromDb)
                .expectNextMatches(retrievedUser -> retrievedUser.getLogin().equals(user.getLogin()))
                .verifyComplete();
    }

    @Test
    void shouldDeleteUser(){
        User user = User.builder()
                .login("test12345")
                .password("test")
                .name("testName")
                .build();

        Mono<User> savedUser = userRepository.save(user);
        Mono<User> userFromDb = userRepository.findUserByLogin(user.getLogin());
        Mono<Void> voidMono = userRepository.deleteUserByLogin(user.getLogin());
        Mono<User> deletedUser = userRepository.findUserByLogin(user.getLogin());

        StepVerifier
                .create(savedUser)
                .expectNextCount(1)
                .verifyComplete();
        StepVerifier
                .create(userFromDb)
                .expectNextMatches(retrievedUser -> retrievedUser.getLogin().equals(user.getLogin()))
                .verifyComplete();
        StepVerifier
                .create(voidMono)
                .verifyComplete();
        StepVerifier
                .create(deletedUser)
                .verifyComplete();

    }
}
