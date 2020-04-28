package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.controller.rest.enums.CPFVoteStatus;
import com.mateusfma.assemblyvoting.controller.rest.response.CPFStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CPFValidationServiceImpl implements CPFValidationService {

    private WebClient validatorClient;

    public CPFValidationServiceImpl(WebClient.Builder builder) {
        validatorClient = builder
                .baseUrl("https://user-info.herokuapp.com")
                .build();
    }

    @Override
    public Mono<Boolean> isValid(Mono<String> cpf) {
        return cpf
                .flatMap(str -> validatorClient
                        .get()
                        .uri("/users/{cpf}", str)
                        .exchange())
                .flatMap(response -> Mono.just(response.statusCode() != HttpStatus.NOT_FOUND))
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> isAbleToVote(Mono<String> cpf) {
        return cpf
                .flatMap(str -> validatorClient
                        .get()
                        .uri("/users/{cpf}", str)
                        .exchange())
                .flatMap(response -> response.toEntity(CPFStatusResponse.class))
                .flatMap(monoStatus -> {
                    try {
                        CPFStatusResponse statusResponse = monoStatus.getBody();

                        if (statusResponse == null)
                            return Mono.just(false);

                        CPFVoteStatus status = CPFVoteStatus.valueOf(statusResponse.getStatus());
                        return Mono.just(status == CPFVoteStatus.ABLE_TO_VOTE);
                    } catch (IllegalArgumentException e) {
                        return Mono.just(false);
                    }
                });
    }
}
