package com.example.processmanagementtool.controller;

import com.example.processmanagementtool.api.v1.UserApi;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserDTO;
import com.example.processmanagementtool.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TestController implements UserApi {

    private final UserService userService;

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> createUser(Mono<UserDTO> userDTO, ServerWebExchange exchange) {
        return userService.saveUser(userDTO)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> deleteUser(Integer id, ServerWebExchange exchange) {
        return userService.deleteUser(id)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<UserDTO>>> getAllUser(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(userService.getALlUsers()));
    }

    @Override
    public Mono<ResponseEntity<Flux<UserDTO>>> getAllUserWithinAgeRange(Integer minAge, Integer maxAge, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(userService.getAllUsersWithinAgeRange(minAge, maxAge)));
    }

    @Override
    public Mono<ResponseEntity<UserDTO>> getOneUser(Integer id, ServerWebExchange exchange) {
        return userService.getOneUser(id)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> updateUser(Integer id, Mono<UserDTO> userDTO, ServerWebExchange exchange) {
        return userService.updateUser(id, userDTO)
                .map(ResponseEntity::ok);
    }
}
