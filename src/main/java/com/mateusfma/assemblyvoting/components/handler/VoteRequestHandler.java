package com.mateusfma.assemblyvoting.components.handler;

import com.mateusfma.assemblyvoting.router.rest.request.VoteRequest;
import com.mateusfma.assemblyvoting.router.rest.response.VoteResponse;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import com.mateusfma.assemblyvoting.service.VoteService;
import com.mateusfma.assemblyvoting.validator.VoteRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class VoteRequestHandler extends BaseValidationHandler<VoteRequest, VoteRequestValidator> {

    @Autowired
    private AssociateService associateService;

    @Autowired
    private CPFValidationService cpfValidationService;

    @Autowired
    private VoteService voteService;

    public VoteRequestHandler() {
        super(VoteRequest.class, new VoteRequestValidator());
    }

    @Override
    protected Mono<? extends ServerResponse> processBody(VoteRequest body) {
        return associateService.retrieveAssociate(body.getAssociateId())
                .flatMap(response -> cpfValidationService.isAbleToVote(Mono.just(response.getCpf())))
                .flatMap(able -> {
                    if (able) {
                        return voteService
                                .receiveVote(Mono.just(body))
                                .flatMap(voteResponse -> ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(Mono.just(voteResponse), VoteResponse.class))
                                .onErrorResume(t -> ServerResponse
                                        .badRequest()
                                        .bodyValue("Não foi possível contabilizar o voto: " +
                                                t.getMessage()));
                    }

                    return ServerResponse
                            .status(HttpStatus.FORBIDDEN)
                            .bodyValue("Associado não está apto a votar.");
                });
    }
}
