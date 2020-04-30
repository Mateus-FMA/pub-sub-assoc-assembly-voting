package com.mateusfma.assemblyvoting.controller;

import com.mateusfma.assemblyvoting.controller.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.controller.rest.request.AssociateUpdateRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.AssociateResponse;
import com.mateusfma.assemblyvoting.exceptions.InvalidCPFException;
import com.mateusfma.assemblyvoting.exceptions.NoSuchAssociateException;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/associate")
public class AssociateController {

    @Autowired
    private CPFValidationService cpfValidationService;

    @Autowired
    private AssociateService associateService;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AssociateResponse> createAssociate(@RequestBody AssociateRequest request) {
        return cpfValidationService.isValid(Mono.just(request.getCpf()))
                        .flatMap(valid -> {
                            if (!valid)
                                throw new InvalidCPFException("CPF inválido.");

                            return associateService.createAssociate(Mono.just(request));
                        });
    }

    @GetMapping(value = "/retrieve/{id}")
    public Mono<AssociateResponse> retrieveAssociate(@PathVariable Long id) {
        return associateService.hasAssociate(id)
                .flatMap(exists -> {
                    if (!exists)
                        throw new NoSuchAssociateException("Associado de ID " + id + " não existe.");

                    return associateService
                            .retrieveAssociate(id)
                            .map(associate -> {
                                AssociateResponse associateResponse = new AssociateResponse();
                                associateResponse.setId(associate.getId());
                                associateResponse.setCpf(associate.getCpf());
                                associateResponse.setName(associate.getName());
                                associateResponse.setAge(associate.getAge());

                                return associateResponse;
                            });
                });
    }

    @PutMapping("/update")
    public Mono<AssociateResponse> updateAssociate(@RequestBody AssociateUpdateRequest request) {
        return associateService.hasAssociate(request.getId())
                    .map(exists -> {
                        if (!exists)
                            throw new NoSuchAssociateException("Associado de ID " + request.getId() + " não existe.");

                        return request;
                    })
                    .flatMap(req -> {
                        if (req.getCpf() != null)
                            return cpfValidationService.isValid(Mono.just(req.getCpf()));
                        else
                            return Mono.just(true);
                    })
                    .flatMap(valid -> {
                        if (!valid)
                            throw new InvalidCPFException("CPF inválido.");

                        return associateService.retrieveAssociate(request.getId());
                    })
                    .flatMap(associate -> {
                        if (request.getCpf() != null)
                            associate.setCpf(request.getCpf());

                        if (request.getName() != null)
                            associate.setName(request.getName());

                        if (request.getAge() != null)
                            associate.setAge(request.getAge());

                        return associateService.updateAssociate(Mono.just(associate));
                    });
    }

    @DeleteMapping(value = "/delete/{id}")
    public Mono<AssociateResponse> deleteAssociate(@PathVariable Long id) {
        return associateService.hasAssociate(id)
                .flatMap(exists -> {
                    if (!exists)
                        throw new NoSuchAssociateException("Associado de ID " + id + " não existe.");
                    return associateService.deleteAssociate(id);
                });
    }
}
