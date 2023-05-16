package com.example.processmanagementtool;

import com.example.processmanagementtool.controller.TestController;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserDTO;
import com.example.processmanagementtool.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@WebFluxTest(TestController.class)
class TestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    public void shouldSaveNewUser(){

        UserDTO userToSave = UserDTO.builder()
                .id(1)
                .age(22)
                .name("Michal")
                .password("passwd")
                .build();

        Mockito.when(userService.saveUser(any())).thenReturn(Mono.just(SuccessResponseDTO.builder().build()));

        webTestClient
                .post()
                .uri("/user")
                .body(Mono.just(userToSave), UserDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldGetUsers() {

        Mockito.when(userService.getALlUsers()).thenReturn(Flux.just(UserDTO.builder().build()));

        webTestClient
                .get().uri("/user")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(UserDTO.class);
    }

    @Test
    public void shouldGetSingleUser(){

        Mockito.when(userService.getOneUser(0)).thenReturn(Mono.just(UserDTO.builder().build()));

        webTestClient.get().uri("/user/0")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDTO.class);
    }

    @Test
    void shouldUpdateUser() {
        UserDTO updateData = UserDTO.builder()
                .id(1)
                .age(22)
                .name("Michal")
                .password("passwd")
                .build();

        Mockito.when(userService.updateUser(eq(0), any()))
                .thenReturn(Mono.just(SuccessResponseDTO.builder().build()));

        webTestClient.put().uri("/user/0")
                .body(Mono.just(updateData), UserDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SuccessResponseDTO.class);
    }

    @Test
    void shouldDeleteUser() {
        Mockito.when(userService.deleteUser(0)).thenReturn(Mono.just(SuccessResponseDTO.builder().build()));

        webTestClient.delete().uri("/user/0")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SuccessResponseDTO.class);
    }

//    @BeforeEach
//    void init(){
//        UserDTO userToSave = UserDTO.builder()
//                .id(1)
//                .age(22)
//                .name("Michal")
//                .password("passwd")
//                .build();
//
//        Mockito.when(userService.saveUser(any())).thenReturn(Mono.just(SuccessResponseDTO.builder().build()));
//
//        webTestClient
//                .post()
//                .uri("/user")
//                .body(Mono.just(userToSave), UserDTO.class)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange();
//    }
}