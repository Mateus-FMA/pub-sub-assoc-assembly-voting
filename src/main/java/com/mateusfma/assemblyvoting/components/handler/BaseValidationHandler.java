package com.mateusfma.assemblyvoting.components.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

public abstract class BaseValidationHandler<T, U extends Validator> {

    private final Class<T> validationClass;

    private final U validator;

    public BaseValidationHandler(Class<T> clazz, U validator) {
        this.validationClass = clazz;
        this.validator = validator;
    }

    public final Mono<ServerResponse> handleRequest(ServerRequest request) {
        return request.bodyToMono(validationClass)
                .flatMap(body -> {
                    Errors errors = new BeanPropertyBindingResult(body, validationClass.getName());
                    validator.validate(body, errors);

                    if (errors.getAllErrors().isEmpty())
                        return processBody(body);
                    else
                        return onValidateErrors(body, errors);
                });
    }

    protected abstract Mono<? extends ServerResponse> processBody(T body);

    private Mono<? extends ServerResponse> onValidateErrors(T body, Errors errors) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getAllErrors().toString());
    }


}
