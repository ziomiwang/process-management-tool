package com.processmanagement.security.service;

import com.processmanagement.domain.user.repository.UserRepository;
import com.processmanagement.security.model.CustomUserDetails;
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
