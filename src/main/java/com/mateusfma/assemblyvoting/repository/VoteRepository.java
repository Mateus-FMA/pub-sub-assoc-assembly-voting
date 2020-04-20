package com.mateusfma.assemblyvoting.repository;

import com.mateusfma.assemblyvoting.entity.Vote;
import reactor.core.publisher.Mono;

public interface VoteRepository {

    Mono<Vote> save(Vote vote);

    Mono<Long> countVotes(Long id, Boolean value);

}
