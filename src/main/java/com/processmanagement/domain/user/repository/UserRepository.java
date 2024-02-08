package com.processmanagement.domain.user.repository;

import com.processmanagement.domain.user.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findUserByLogin(String login);

    Mono<Void> deleteUserByLogin(String login);

    Flux<User> findAllByLoginIn(Set<String> login);

    Flux<User> findAllByTeamId(Long teamId);

    Mono<Boolean> existsUserByLogin(String login);
}
