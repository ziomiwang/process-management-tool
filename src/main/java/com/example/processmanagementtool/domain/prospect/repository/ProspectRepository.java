package com.example.processmanagementtool.domain.prospect.repository;

import com.example.processmanagementtool.domain.prospect.Prospect;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProspectRepository extends ReactiveCrudRepository<Prospect, Long> {
}
