package com.example.processmanagementtool.security.service;

import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationUserService {

    private final UserRepository userRepository;

    public Mono<CustomUserDetails> loadUserByLogin(String login) {
        return userRepository.findUserByLogin(login)
                .map(CustomUserDetails::new);
    }
}
