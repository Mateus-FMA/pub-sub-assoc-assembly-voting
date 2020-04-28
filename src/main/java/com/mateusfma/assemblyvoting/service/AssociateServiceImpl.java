package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.controller.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.AssociateResponse;
import com.mateusfma.assemblyvoting.entity.Associate;
import com.mateusfma.assemblyvoting.repository.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AssociateServiceImpl implements AssociateService {

    @Autowired
    private AssociateRepository repository;

    @Override
    public Mono<Boolean> hasAssociate(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<AssociateResponse> createAssociate(Mono<AssociateRequest> request) {
        return request
                .map(associateRequest -> {
                    Associate associate = new Associate();
                    associate.setCpf(associateRequest.getCpf());
                    associate.setName(associateRequest.getName());
                    associate.setAge(associateRequest.getAge());

                    return associate;
                })
                .flatMap(associate -> repository.save(associate))
                .map(associate -> {
                    AssociateResponse response = new AssociateResponse();
                    response.setId(associate.getId());
                    response.setCpf(associate.getCpf());
                    response.setName(associate.getName());
                    response.setAge(associate.getAge());

                    return response;
                });
    }

    @Override
    public Mono<Associate> retrieveAssociate(Long id) {
        return repository.findById(id);
    }

    @Override
    public Mono<AssociateResponse> updateAssociate(Mono<Associate> associate) {
        return associate
                .flatMap(a -> repository.save(a))
                .map(a -> {
                    AssociateResponse response = new AssociateResponse();
                    response.setId(a.getId());
                    response.setCpf(a.getCpf());
                    response.setName(a.getName());
                    response.setAge(a.getAge());

                    return response;
                });
    }

    @Override
    public Mono<AssociateResponse> deleteAssociate(Long id) {
        return repository
                .deleteByIdReturning(id)
                .map(associate -> {
                    AssociateResponse response = new AssociateResponse();
                    response.setId(associate.getId());
                    response.setCpf(associate.getCpf());
                    response.setName(associate.getName());
                    response.setAge(associate.getAge());

                    return response;
                });


    }
}
