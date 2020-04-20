package com.mateusfma.assemblyvoting.repository;

import com.mateusfma.assemblyvoting.entity.Topic;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TopicRepository extends ReactiveCrudRepository<Topic, Long> {

    @Query(value = "SELECT * FROM topic WHERE topic_name = $1")
    Mono<Topic> findByName(String name);

}
