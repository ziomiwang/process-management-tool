package com.example.processmanagementtool.domain.prospect.repository;

import com.example.processmanagementtool.domain.prospect.Prospect;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProspectRepository extends ReactiveSortingRepository<Prospect, Long>, ReactiveCrudRepository<Prospect, Long> {

    Flux<Prospect> findAllByUserId(Long userId);
    Flux<Prospect> findAllByUserId(Long userId, Pageable pageable);
}
