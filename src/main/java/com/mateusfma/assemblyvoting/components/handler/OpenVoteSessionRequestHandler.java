package com.mateusfma.assemblyvoting.components.handler;

import com.mateusfma.assemblyvoting.router.rest.request.OpenVoteSessionRequest;
import com.mateusfma.assemblyvoting.router.rest.response.TopicResponse;
import com.mateusfma.assemblyvoting.service.TopicService;
import com.mateusfma.assemblyvoting.validator.OpenVoteSessionRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class OpenVoteSessionRequestHandler
        extends BaseValidationHandler<OpenVoteSessionRequest, OpenVoteSessionRequestValidator> {

    @Autowired
    private TopicService service;

    public OpenVoteSessionRequestHandler() {
        super(OpenVoteSessionRequest.class, new OpenVoteSessionRequestValidator());
    }

    @Override
    protected Mono<? extends ServerResponse> processBody(OpenVoteSessionRequest body) {
        return service
                .openVoteSession(Mono.just(body))
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(response), TopicResponse.class))
                .onErrorResume(t -> ServerResponse
                        .badRequest()
                        .bodyValue("Não foi possível abrir a pauta para votação: " +
                                t.getMessage()));
    }
}
