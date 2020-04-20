package com.mateusfma.assemblyvoting.service;

import reactor.core.publisher.Mono;

public interface CPFValidationService {

    Mono<Boolean> isValid(Mono<String> cpf);

    Mono<Boolean> isAbleToVote(Mono<String> cpf);

}
