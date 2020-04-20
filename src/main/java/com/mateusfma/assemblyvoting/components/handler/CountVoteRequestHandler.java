package com.mateusfma.assemblyvoting.components.handler;

import com.mateusfma.assemblyvoting.router.rest.request.CountVoteRequest;
import com.mateusfma.assemblyvoting.router.rest.response.VoteResponse;
import com.mateusfma.assemblyvoting.service.VoteService;
import com.mateusfma.assemblyvoting.validator.CountVoteRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CountVoteRequestHandler extends BaseValidationHandler<CountVoteRequest, CountVoteRequestValidator> {

    @Autowired
    private VoteService voteService;

    public CountVoteRequestHandler() {
        super(CountVoteRequest.class, new CountVoteRequestValidator());
    }

    @Override
    protected Mono<? extends ServerResponse> processBody(CountVoteRequest body) {
        return voteService
                .countVotes(Mono.just(body))
                .flatMap(voteResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(voteResponse), VoteResponse.class))
                .onErrorResume(t -> ServerResponse
                        .badRequest()
                        .bodyValue("Não foi possível contabilizar os votos: " +
                                t.getMessage()));
    }
}
