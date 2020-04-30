package com.mateusfma.assemblyvoting.service;


import com.mateusfma.assemblyvoting.controller.rest.request.AssociateRequest;
import com.mateusfma.assemblyvoting.entity.Associate;
import com.mateusfma.assemblyvoting.repository.AssociateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class AssociateServiceTest {

    @InjectMocks
    private AssociateServiceImpl service;

    @Mock
    private AssociateRepository repository;

    @Test
    void testHasAssociate() {
        Mockito.when(repository.existsById(ArgumentMatchers.eq(1L))).thenReturn(Mono.just(true));
        StepVerifier
                .create(service.hasAssociate(1L))
                .expectNext(true)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .existsById(ArgumentMatchers.eq(1L));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testCreateAssociate() {
        Associate associate = new Associate();
        associate.setId(1L);
        associate.setCpf("71781866074");
        associate.setName("Edward Elric");
        associate.setAge(17);

        Mockito.when(repository.save(ArgumentMatchers.any(Associate.class))).thenReturn(Mono.just(associate));

        AssociateRequest request = new AssociateRequest();
        request.setCpf("71781866074");
        request.setName("Edward Elric");
        request.setAge(17);

        StepVerifier
                .create(service.createAssociate(Mono.just(request)))
                .expectNextMatches(response -> Objects.equals(response.getCpf(), "71781866074")
                                && Objects.equals(response.getName(), "Edward Elric")
                                && Objects.equals(response.getAge(), 17)
                                && Objects.equals(response.getId(), 1L)
                        )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .save(ArgumentMatchers.any(Associate.class));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testRetrieveAssociate() {
        Associate associate = new Associate();
        associate.setId(1L);
        associate.setCpf("71781866074");
        associate.setName("Edward Elric");
        associate.setAge(17);

        Mockito.when(repository.findById(ArgumentMatchers.eq(1L))).thenReturn(Mono.just(associate));

        StepVerifier
                .create(service.retrieveAssociate(1L))
                .expectNextMatches(response -> Objects.equals(response.getCpf(), "71781866074")
                        && Objects.equals(response.getName(), "Edward Elric")
                        && Objects.equals(response.getAge(), 17)
                        && Objects.equals(response.getId(), 1L)
                )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .findById(ArgumentMatchers.eq(1L));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testUpdateAssociate() {
        Associate associate = new Associate();
        associate.setId(1L);
        associate.setCpf("71781866074");
        associate.setName("Edward Elric");
        associate.setAge(17);

        Mockito.when(repository.save(ArgumentMatchers.any(Associate.class))).thenReturn(Mono.just(associate));
        StepVerifier
                .create(service.updateAssociate(Mono.just(associate)))
                .expectNextMatches(response -> Objects.equals(response.getCpf(), "71781866074")
                        && Objects.equals(response.getName(), "Edward Elric")
                        && Objects.equals(response.getAge(), 17)
                        && Objects.equals(response.getId(), 1L)
                )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .save(ArgumentMatchers.any(Associate.class));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testDeleteAssociate() {
        Associate associate = new Associate();
        associate.setId(1L);
        associate.setCpf("71781866074");
        associate.setName("Edward Elric");
        associate.setAge(17);

        Mockito.when(repository.deleteByIdReturning(ArgumentMatchers.eq(1L))).thenReturn(Mono.just(associate));

        StepVerifier
                .create(service.deleteAssociate(1L))
                .expectNextMatches(response -> Objects.equals(response.getCpf(), "71781866074")
                        && Objects.equals(response.getName(), "Edward Elric")
                        && Objects.equals(response.getAge(), 17)
                        && Objects.equals(response.getId(), 1L)
                )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .deleteByIdReturning(ArgumentMatchers.eq(1L));
        Mockito.verifyNoMoreInteractions(repository);
    }
}
