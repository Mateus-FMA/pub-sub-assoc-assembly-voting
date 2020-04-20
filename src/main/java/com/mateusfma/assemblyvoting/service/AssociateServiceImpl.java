package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.entity.Associate;
import com.mateusfma.assemblyvoting.repository.AssociateRepository;
import com.mateusfma.assemblyvoting.router.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.router.rest.request.AssociateUpdateRequest;
import com.mateusfma.assemblyvoting.router.rest.response.AssociateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.function.Tuple2;

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
    public Mono<AssociateResponse> retrieveAssociate(Long id) {
        return repository.findById(id)
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
    public Mono<AssociateResponse> updateAssociate(Mono<AssociateUpdateRequest> request) {
        return request
                .flatMap(updateRequest -> Mono.zip(repository.findById(updateRequest.getId()), request))
                .flatMap(objs -> {
                    Associate associate = objs.getT1();
                    AssociateUpdateRequest req = objs.getT2();

                    if (req.getCpf() != null)
                        associate.setCpf(req.getCpf());

                    if (req.getName() != null)
                        associate.setName(req.getName());

                    if (req.getAge() != null)
                        associate.setAge(req.getAge());

                    return repository.save(associate);
                })
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
