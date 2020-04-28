package com.mateusfma.assemblyvoting.controller;

import com.mateusfma.assemblyvoting.controller.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.controller.rest.request.AssociateUpdateRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.AssociateResponse;
import com.mateusfma.assemblyvoting.exceptions.InvalidRequestException;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Mono<AssociateResponse>> createAssociate(@RequestBody AssociateRequest request) {
        Mono<AssociateResponse> response = cpfValidationService.isValid(Mono.just(request.getCpf()))
                .flatMap(valid -> {
                    if (!valid)
                        throw new InvalidRequestException("CPF inválido.");

                    return associateService.createAssociate(Mono.just(request));
                });

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/retrieve/{id}")
    public ResponseEntity<Mono<AssociateResponse>> retrieveAssociate(@PathVariable Long id) {
        Mono<AssociateResponse> response = associateService.hasAssociate(id)
                .flatMap(exists -> {
                    if (!exists)
                        throw new InvalidRequestException("Associado de ID " + id + " não existe.");

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

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Mono<AssociateResponse>> updateAssociate(@RequestBody AssociateUpdateRequest request) {
        Mono<AssociateResponse> response = Mono.just(request)
                .flatMap(req -> {
                    if (req.getCpf() != null)
                        return cpfValidationService.isValid(Mono.just(req.getCpf()));
                    else
                        return Mono.just(true);
                })
                .flatMap(valid -> {
                    if (!valid)
                        throw new InvalidRequestException("CPF inválido.");

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

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Mono<AssociateResponse>> deleteAssociate(@PathVariable Long id) {
        Mono<AssociateResponse> response = associateService.hasAssociate(id)
                .flatMap(exists -> {
                    if (!exists)
                        throw new InvalidRequestException("Associado de ID " + id + " não existe.");
                    return associateService.deleteAssociate(id);
                });

        return ResponseEntity.ok().body(response);
    }
}
