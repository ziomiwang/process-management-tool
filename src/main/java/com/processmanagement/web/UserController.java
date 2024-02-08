package com.processmanagement.web;

import com.example.processmanagementtool.api.v1.UserApi;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.dto.UserResponseDTO;
import com.processmanagement.exception.customexceptions.UserNotFoundException;
import com.processmanagement.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> deleteUser(Long id, ServerWebExchange exchange) {
        return userService.deleteUser(id)
//                .map(ResponseEntity::ok);
                .map(user -> {
                    if (user == null) {
                        throw new UserNotFoundException("User not found");
                    }
                    return new ResponseEntity<>(user, HttpStatus.OK);
                });

    }

    @Override
    public Mono<ResponseEntity<Flux<UserResponseDTO>>> getAllUser(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(userService.getALlUsers()));
    }

    @Override
    public Mono<ResponseEntity<UserResponseDTO>> getOneUser(Long id, ServerWebExchange exchange) {
        return userService.getOneUser(id)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> updateUser(Long id, Mono<UserRequestDTO> userDTO, ServerWebExchange exchange) {
        return userService.updateUser(id, userDTO)
                .map(ResponseEntity::ok);
    }
}
