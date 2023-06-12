package com.example.processmanagementtool.web;

import com.example.processmanagementtool.api.v1.SecurityApi;
import com.example.processmanagementtool.dto.AuthRequest;
import com.example.processmanagementtool.dto.AuthResponse;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.user.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserAuthController implements SecurityApi {

    private final UserAuthService userAuthService;

    @Override
    public Mono<ResponseEntity<AuthResponse>> loginUser(Mono<AuthRequest> authRequest, ServerWebExchange exchange) {
        return userAuthService.authenticateUser(authRequest)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<AuthResponse>> registerUser(Mono<UserRequestDTO> userRequestDTO, ServerWebExchange exchange) {
        return userAuthService.registerUser(userRequestDTO)
                .map(ResponseEntity::ok);
    }
}
