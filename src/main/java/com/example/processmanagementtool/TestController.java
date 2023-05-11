package com.example.processmanagementtool;

import com.example.processmanagementtool.api.v1.UserApi;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class TestController implements UserApi {

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> createUser(Mono<UserDTO> userDTO, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> deleteUser(String id, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> getAllUser(ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> getAllUserWithinAgeRange(Integer minAge, Integer maxAge, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> getByUserName(String userName, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> getOneUser(String id, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> updateUser(String id, Mono<UserDTO> userDTO, ServerWebExchange exchange) {
        return null;
    }
}
