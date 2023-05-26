package com.example.processmanagementtool.repository;

import com.example.processmanagementtool.domain.user.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findUserByLogin(String login);
    Mono<Void> deleteUserByLogin(String login);
}
