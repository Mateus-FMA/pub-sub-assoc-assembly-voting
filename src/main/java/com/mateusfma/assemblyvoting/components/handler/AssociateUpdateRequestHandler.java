package com.mateusfma.assemblyvoting.components.handler;

import com.mateusfma.assemblyvoting.router.rest.request.AssociateUpdateRequest;
import com.mateusfma.assemblyvoting.router.rest.response.AssociateResponse;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import com.mateusfma.assemblyvoting.validator.AssociateUpdateRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AssociateUpdateRequestHandler
        extends BaseValidationHandler<AssociateUpdateRequest, AssociateUpdateRequestValidator> {

    @Autowired
    private CPFValidationService cpfValidationService;

    @Autowired
    private AssociateService associateService;


    public AssociateUpdateRequestHandler() {
        super(AssociateUpdateRequest.class, new AssociateUpdateRequestValidator());
    }

    @Override
    protected Mono<? extends ServerResponse> processBody(AssociateUpdateRequest body) {
        return Mono.just(body)
                .flatMap(req -> {
                    if (req.getCpf() != null)
                        return cpfValidationService.isValid(Mono.just(req.getCpf()));
                    else
                        return Mono.just(true);
                })
                .flatMap(valid -> {
                    if (valid) {
                        return associateService
                                .updateAssociate(Mono.just(body))
                                .flatMap(response -> ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(Mono.just(response), AssociateResponse.class))
                                .onErrorResume(t -> ServerResponse
                                        .badRequest()
                                        .bodyValue("Não foi possível atualizar o associado: " +
                                                t.getMessage()));
                    }

                    return ServerResponse.badRequest().bodyValue("CPF inválido.");
                });
    }
}
