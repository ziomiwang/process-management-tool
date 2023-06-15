package com.example.processmanagementtool;

import com.example.config.WebfluxConfigurationTest;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest
@Import(WebfluxConfigurationTest.class)
class TestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldSaveNewUser() {
        UserRequestDTO userToSave = UserRequestDTO.builder()
                .login("test")
                .password("passwd")
                .name("Michal")
                .build();

        webTestClient
                .post()
                .uri("/user")
                .body(Mono.just(userToSave), UserRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SuccessResponseDTO.class)
                .isEqualTo(SuccessResponseDTO.builder().message("saved user with login " + userToSave.getLogin()).build());
    }

    @Test
    public void shouldGetUsers() {

        webTestClient
                .get().uri("/user")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(UserResponseDTO.class);
    }

    @Test
    public void shouldGetSingleUser() {

        webTestClient.get().uri("/user/0")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserResponseDTO.class);
    }

    @Test
    void shouldUpdateUser() {
        UserRequestDTO updateData = UserRequestDTO.builder()
                .login("test")
                .name("Michal")
                .password("test")
                .build();

        webTestClient.put().uri("/user/0")
                .body(Mono.just(updateData), UserRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SuccessResponseDTO.class);
    }

    @Test
    void shouldDeleteUser() {

        webTestClient.delete().uri("/user/0")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SuccessResponseDTO.class);
    }
}