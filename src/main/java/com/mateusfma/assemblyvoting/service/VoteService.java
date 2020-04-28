package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.controller.rest.response.CountVoteResponse;
import com.mateusfma.assemblyvoting.entity.Vote;
import reactor.core.publisher.Mono;

public interface VoteService {

    Mono<Vote> receiveVote(Long associateId, Long topicId, String value);

    Mono<CountVoteResponse> countVotes(Long topicId);

}
