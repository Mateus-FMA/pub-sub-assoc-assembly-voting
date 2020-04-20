package com.mateusfma.assemblyvoting.components.handler;

import com.mateusfma.assemblyvoting.router.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.router.rest.response.AssociateResponse;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import com.mateusfma.assemblyvoting.validator.AssociateRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AssociateRequestHandler extends BaseValidationHandler<AssociateRequest, AssociateRequestValidator> {

    @Autowired
    private AssociateService associateService;

    @Autowired
    private CPFValidationService cpfValidationService;

    public AssociateRequestHandler() {
        super(AssociateRequest.class, new AssociateRequestValidator());
    }

    @Override
    protected Mono<? extends ServerResponse> processBody(AssociateRequest body) {
        return cpfValidationService.isValid(Mono.just(body.getCpf()))
                .flatMap(valid -> {
                    if (valid) {
                        return associateService
                                .createAssociate(Mono.just(body))
                                .flatMap(response -> ServerResponse
                                        .status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(Mono.just(response), AssociateResponse.class))
                                .onErrorResume(t -> ServerResponse
                                        .badRequest()
                                        .bodyValue("Não foi possível criar o associado: " +
                                                t.getMessage()));
                    }

                    return ServerResponse.badRequest().bodyValue("CPF inválido.");
                });
    }
}
