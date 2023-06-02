package com.example.processmanagementtool.domain.user.repository;

import com.example.processmanagementtool.domain.user.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findUserByLogin(String login);
    Mono<Void> deleteUserByLogin(String login);
    Flux<User> findAllByLoginIn(List<String> login);
    Flux<User> findAllByTeamId(Long teamId);
}
