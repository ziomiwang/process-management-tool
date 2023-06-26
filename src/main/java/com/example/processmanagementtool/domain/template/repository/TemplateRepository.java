package com.example.processmanagementtool.domain.template.repository;

import com.example.processmanagementtool.domain.template.Template;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TemplateRepository extends ReactiveCrudRepository<Template, Long> {

    Flux<Template> findAllByUserId(Long userId);
    Mono<Template> findTemplateByUserIdAndId(Long userId, Long id);
}
