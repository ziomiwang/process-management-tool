package com.example.processmanagementtool.domain.user.repository;

import com.example.processmanagementtool.domain.user.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findUserByLogin(String login);
    Mono<Void> deleteUserByLogin(String login);
    Flux<User> findAllByLoginIn(Set<String> login);
    Flux<User> findAllByTeamId(Long teamId);
    Mono<Boolean> existsUserByLogin(String login);
}
