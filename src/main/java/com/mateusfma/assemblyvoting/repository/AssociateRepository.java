package com.mateusfma.assemblyvoting.repository;

import com.mateusfma.assemblyvoting.entity.Associate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AssociateRepository extends ReactiveCrudRepository<Associate, Long> {

    @Query("DELETE FROM associate WHERE associate_id = $1 RETURNING *")
    Mono<Associate> deleteByIdReturning(Long id);

}
