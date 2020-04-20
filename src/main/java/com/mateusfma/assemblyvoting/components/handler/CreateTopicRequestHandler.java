package com.mateusfma.assemblyvoting.components.handler;

import com.mateusfma.assemblyvoting.router.rest.request.CreateTopicRequest;
import com.mateusfma.assemblyvoting.router.rest.response.TopicResponse;
import com.mateusfma.assemblyvoting.service.TopicService;
import com.mateusfma.assemblyvoting.validator.CreateTopicRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CreateTopicRequestHandler extends BaseValidationHandler<CreateTopicRequest, CreateTopicRequestValidator> {

    @Autowired
    private TopicService service;

    public CreateTopicRequestHandler() {
        super(CreateTopicRequest.class, new CreateTopicRequestValidator());
    }

    @Override
    protected Mono<? extends ServerResponse> processBody(CreateTopicRequest body) {
        return service.createTopic(Mono.just(body))
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(response), TopicResponse.class))
                .onErrorResume(t -> ServerResponse
                        .badRequest()
                        .bodyValue("Não foi possível criar a pauta: " + t.getMessage()));
    }
}
