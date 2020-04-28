package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.controller.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.AssociateResponse;
import com.mateusfma.assemblyvoting.entity.Associate;
import reactor.core.publisher.Mono;

public interface AssociateService {

    Mono<Boolean> hasAssociate(Long id);

    Mono<AssociateResponse> createAssociate(Mono<AssociateRequest> request);

    Mono<Associate> retrieveAssociate(Long id);

    Mono<AssociateResponse> updateAssociate(Mono<Associate> associate);

    Mono<AssociateResponse> deleteAssociate(Long id);

}
