package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.router.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.router.rest.request.AssociateUpdateRequest;
import com.mateusfma.assemblyvoting.router.rest.response.AssociateResponse;
import reactor.core.publisher.Mono;

public interface AssociateService {

    Mono<Boolean> hasAssociate(Long id);

    Mono<AssociateResponse> createAssociate(Mono<AssociateRequest> request);

    Mono<AssociateResponse> retrieveAssociate(Long id);

    Mono<AssociateResponse> updateAssociate(Mono<AssociateUpdateRequest> request);

    Mono<AssociateResponse> deleteAssociate(Long id);

}
