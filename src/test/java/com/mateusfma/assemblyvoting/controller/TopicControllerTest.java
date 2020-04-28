package com.mateusfma.assemblyvoting.controller;

import com.mateusfma.assemblyvoting.components.manager.VoteSessionManager;
import com.mateusfma.assemblyvoting.controller.rest.request.CreateTopicRequest;
import com.mateusfma.assemblyvoting.controller.rest.request.OpenVoteSessionRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.TopicResponse;
import com.mateusfma.assemblyvoting.entity.Topic;
import com.mateusfma.assemblyvoting.repository.TopicRepository;
import com.mateusfma.assemblyvoting.service.TopicService;
import com.mateusfma.assemblyvoting.service.TopicServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.time.OffsetDateTime;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TopicController.class)
class TopicControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private TopicService service;

    @MockBean
    private VoteSessionManager manager;

    @Test
    void testCreateTopic() {
        TopicResponse toReturn = new TopicResponse();
        toReturn.setId(1L);
        toReturn.setName("Tópico");
        toReturn.setOpen(false);

        Mockito.when(service.createTopic(ArgumentMatchers.any())).thenReturn(Mono.just(toReturn));

        CreateTopicRequest request = new CreateTopicRequest();
        request.setName("Tópico");

        client.post()
                .uri("/topic/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.name").isEqualTo("Tópico")
                .jsonPath("$.open").isEqualTo(false);

        Mockito.verify(service, Mockito.times(1))
                .createTopic(ArgumentMatchers.any());
    }

    @Test
    void testOpenVoteSession() {
        Topic toReturn = new Topic();
        toReturn.setId(1L);
        toReturn.setName("Tópico");
        toReturn.setOpen(false);

        TopicResponse response = new TopicResponse();
        response.setId(1L);
        response.setName("Tópico");
        response.setOpen(true);
        response.setStart(Date.from(OffsetDateTime.now().toInstant()));
        response.setDurationSec(120);

        Mockito.when(service.findByName(ArgumentMatchers.any())).thenReturn(Mono.just(toReturn));
        Mockito.when(service.openVoteSession(ArgumentMatchers.any())).thenReturn(Mono.just(response));

        OpenVoteSessionRequest request = new OpenVoteSessionRequest();
        request.setTopicName("Tópico");
        request.setDurationSec(120);

        client.post()
                .uri("/topic/open")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.name").isEqualTo("Tópico")
                .jsonPath("$.open").isEqualTo(true)
                .jsonPath("$.start").isNotEmpty()
                .jsonPath("$.durationSec").isEqualTo(120);

        Mockito.verify(service, Mockito.times(1)).findByName(ArgumentMatchers.any());
        Mockito.verify(service, Mockito.times(1)).openVoteSession(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    void testAlreadyOpenedVoteSession() {
        Topic toReturn = new Topic();
        toReturn.setId(1L);
        toReturn.setName("Tópico");
        toReturn.setOpen(true);
        toReturn.setStart(OffsetDateTime.now());
        toReturn.setDurationSec(120);

        Mockito.when(service.findByName(ArgumentMatchers.any())).thenReturn(Mono.just(toReturn));

        OpenVoteSessionRequest request = new OpenVoteSessionRequest();
        request.setTopicName("Tópico");
        request.setDurationSec(120);

        client.post()
                .uri("/topic/open")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().is5xxServerError();

        Mockito.verify(service, Mockito.times(1)).findByName(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(service);
    }
}
