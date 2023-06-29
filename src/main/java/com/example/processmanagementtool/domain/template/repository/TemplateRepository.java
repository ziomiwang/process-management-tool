package com.example.processmanagementtool.domain.template.repository;

import com.example.processmanagementtool.domain.template.Template;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TemplateRepository extends ReactiveSortingRepository<Template, Long>, ReactiveCrudRepository<Template, Long> {

    Flux<Template> findAllByUserId(Long userId);

    Flux<Template> findAllByUserId(Long userId, Pageable pageable);

    Mono<Template> findTemplateByUserIdAndId(Long userId, Long id);
}
