package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.router.rest.request.CountVoteRequest;
import com.mateusfma.assemblyvoting.router.rest.request.VoteRequest;
import com.mateusfma.assemblyvoting.router.rest.response.CountVoteResponse;
import com.mateusfma.assemblyvoting.router.rest.response.VoteResponse;
import reactor.core.publisher.Mono;

public interface VoteService {

    Mono<VoteResponse> receiveVote(Mono<VoteRequest> request);

    Mono<CountVoteResponse> countVotes(Mono<CountVoteRequest> request);

}
