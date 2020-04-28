package com.mateusfma.assemblyvoting.controller;

import com.mateusfma.assemblyvoting.controller.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.controller.rest.request.AssociateUpdateRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.AssociateResponse;
import com.mateusfma.assemblyvoting.entity.Associate;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AssociateController.class)
class AssociateControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private AssociateService associateService;

    @MockBean
    private CPFValidationService cpfValidationService;

//    @Test
//    void testCreateAssociate() {
//        Mono<AssociateResponse> response = Mono.defer(() -> {
//                AssociateResponse r = new AssociateResponse();
//                r.setId(1L);
//                r.setCpf("71781866074");
//                r.setName("Edward Elric");
//                r.setAge(17);
//
//                return Mono.just(r);
//            });
//
//        AssociateRequest request = new AssociateRequest();
//        request.setCpf("71781866074");
//        request.setName("Edward Elric");
//        request.setAge(17);
//
//        Mono<String> cpf = Mono.just("71781866074");
//
//        Mockito.when(associateService.createAssociate(ArgumentMatchers.eq(Mono.just(request)))).thenReturn(response);
//        Mockito.when(cpfValidationService.isValid(ArgumentMatchers.isNotNull())).thenReturn(Mono.just(true));
//        Mockito.when(cpfValidationService.isValid(ArgumentMatchers.isNull())).thenReturn(Mono.just(false));
//
//        client.post()
//                .uri("/associate/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(request))
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody()
//                .jsonPath("$.id").isEqualTo(1L)
//                .jsonPath("$.cpf").isEqualTo("71781866074")
//                .jsonPath("$.name").isEqualTo("Edward Elric")
//                .jsonPath("$.age").isEqualTo(17);
//
//        Mockito.verify(associateService, Mockito.times(1))
//                .createAssociate(Mono.just(request));
//        Mockito.verify(cpfValidationService, Mockito.times(1)).isValid(cpf);
//    }
//
//    @Test
//    void testRetrieveAssociate() {
//        Associate associate = new Associate();
//        associate.setId(1L);
//        associate.setCpf("71781866074");
//        associate.setName("Edward Elric");
//        associate.setAge(17);
//
//        Mockito.when(associateService.hasAssociate(ArgumentMatchers.any())).thenReturn(Mono.just(true));
//        Mockito.when(associateService.retrieveAssociate(ArgumentMatchers.any())).thenReturn(Mono.just(associate));
//
//        client.get()
//                .uri("/response/retrieve/{id}", 1)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.id").isEqualTo(1L)
//                .jsonPath("$.cpf").isEqualTo("71781866074")
//                .jsonPath("$.name").isEqualTo("Edward Elric")
//                .jsonPath("$.age").isEqualTo(17);
//
//        Mockito.verify(associateService, Mockito.times(1)).retrieveAssociate(1L);
//    }
//
//    @Test
//    void testUpdateAssociate() {
//        Associate associate = new Associate();
//        associate.setId(1L);
//        associate.setCpf("71781866074");
//        associate.setName("Edward Elric");
//        associate.setAge(17);
//
//        Mono<AssociateResponse> response = Mono.defer(() -> {
//            AssociateResponse r = new AssociateResponse();
//            r.setId(1L);
//            r.setCpf("71781866074");
//            r.setName("Edward Elric");
//            r.setAge(17);
//
//            return Mono.just(r);
//        });
//
//        Mockito.when(associateService.retrieveAssociate(ArgumentMatchers.isNotNull())).thenReturn(Mono.just(associate));
//        Mockito.when(associateService.updateAssociate(Mono.just(associate))).thenReturn(response);
//        Mockito.when(cpfValidationService.isValid(ArgumentMatchers.isNotNull())).thenReturn(Mono.just(true));
//        Mockito.when(cpfValidationService.isValid(ArgumentMatchers.isNull())).thenReturn(Mono.just(false));
//
//        AssociateUpdateRequest request = new AssociateUpdateRequest();
//        request.setId(1L);
//        request.setAge(17);
//
//        client.put()
//                .uri("/associate/update")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(request))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.id").isEqualTo(1L)
//                .jsonPath("$.cpf").isEqualTo("71781866074")
//                .jsonPath("$.name").isEqualTo("Edward Elric")
//                .jsonPath("$.age").isEqualTo(17);
//
//        Mockito.verify(associateService, Mockito.times(1)).updateAssociate(Mono.just(associate));
//    }
//
//    @Test
//    void testDeleteAssociate() {
//        Associate associate = new Associate();
//        associate.setId(1L);
//        associate.setCpf("71781866074");
//        associate.setName("Edward Elric");
//        associate.setAge(17);
//
//        Mockito.when(repository.findById(1L)).thenReturn(Mono.just(associate));
//
//        client.delete()
//                .uri("/associate/delete/{id}", 1L)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.id").isEqualTo(1L)
//                .jsonPath("$.cpf").isEqualTo("71781866074")
//                .jsonPath("$.name").isEqualTo("Edward Elric")
//                .jsonPath("$.age").isEqualTo(17);
//
//        Mockito.verify(repository, Mockito.times(1)).findById(1L);
//    }
}
