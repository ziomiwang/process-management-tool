package com.processmanagement.domain.team.repository;


import com.processmanagement.domain.team.Team;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TeamRepository extends ReactiveCrudRepository<Team, Long> {
    Mono<Team> findTeamByOwnerId(Long ownerId);
}
