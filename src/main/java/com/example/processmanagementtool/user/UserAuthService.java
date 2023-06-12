package com.example.processmanagementtool.user;

import com.example.processmanagementtool.PBKDF2Encoder;
import com.example.processmanagementtool.domain.user.Role;
import com.example.processmanagementtool.domain.user.User;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.dto.AuthRequest;
import com.example.processmanagementtool.dto.AuthResponse;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.exception.customexceptions.BadRequest;
import com.example.processmanagementtool.exception.customexceptions.InvalidCredentialsException;
import com.example.processmanagementtool.exception.customexceptions.LoginAlreadyTakenException;
import com.example.processmanagementtool.security.model.CustomUserDetails;
import com.example.processmanagementtool.security.service.AuthenticationUserService;
import com.example.processmanagementtool.utlitiy.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final JWTUtil jwtUtil;
    private final PBKDF2Encoder passwordEncoder;
    private final AuthenticationUserService authService;

    private final UserRepository userRepository;

    public Mono<AuthResponse> registerUser(Mono<UserRequestDTO> request) {
        return request.flatMap(this::checkUser);
    }

    private Mono<AuthResponse> checkUser(UserRequestDTO request) {
        final Function<Boolean, Mono<AuthResponse>> userPropagation = exists -> exists ?
                Mono.empty() :
                saveNewUser(request);

        return userRepository.existsUserByLogin(request.getLogin())
                .flatMap(userPropagation)
                .switchIfEmpty(Mono.error(new LoginAlreadyTakenException()));
    }

    private Mono<AuthResponse> saveNewUser(UserRequestDTO request) {
        User user = User.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user)
                .flatMap(savedUser -> authService.loadUserByLogin(savedUser.getLogin()))
                .flatMap(this::mapUserToAuthResponse);
    }

    private Mono<AuthResponse> mapUserToAuthResponse(CustomUserDetails authenticatedUser) {
        User user = authenticatedUser.getUser();

        return Mono.just(AuthResponse.builder()
                .token(jwtUtil.generateToken(user))
                .build());
    }

    public Mono<AuthResponse> authenticateUser(Mono<AuthRequest> authRequest) {
        return authRequest.flatMap(request -> authService.loadUserByLogin(request.getLogin())
                .filter(userDetails -> validateRequest(userDetails, request))
                .flatMap(this::mapUserToAuthResponse)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException())));
    }

    private boolean validateRequest(CustomUserDetails userDetails, AuthRequest request) {
        return Objects.nonNull(userDetails.getPassword()) && passwordEncoder.encode(request.getPassword()).equals(userDetails.getPassword());
    }
}
