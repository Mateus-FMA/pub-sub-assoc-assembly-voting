package com.mateusfma.assemblyvoting.controller;

import com.mateusfma.assemblyvoting.controller.rest.enums.VoteValue;
import com.mateusfma.assemblyvoting.controller.rest.request.VoteRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.CountVoteResponse;
import com.mateusfma.assemblyvoting.entity.Associate;
import com.mateusfma.assemblyvoting.entity.Topic;
import com.mateusfma.assemblyvoting.entity.Vote;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import com.mateusfma.assemblyvoting.service.TopicService;
import com.mateusfma.assemblyvoting.service.VoteService;
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

import java.time.OffsetDateTime;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = VoteController.class)
public class VoteControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private AssociateService associateService;

    @MockBean
    private TopicService topicService;

    @MockBean
    private CPFValidationService cpfValidationService;

    @MockBean
    private VoteService voteService;

    @Test
    void testReceiveVote() {
        Associate associate = new Associate();
        associate.setId(1L);
        associate.setCpf("71781866074");
        associate.setName("Edward Elric");
        associate.setAge(17);

        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Tópico");
        topic.setOpen(true);
        topic.setStart(OffsetDateTime.now());
        topic.setDurationSec(120);

        Vote vote = new Vote();
        vote.setAssociateId(1L);
        vote.setTopicId(1L);
        vote.setValue(VoteValue.YES.getValue());

        Mockito.when(associateService.retrieveAssociate(ArgumentMatchers.eq(1L))).thenReturn(Mono.just(associate));
        Mockito.when(topicService.findByName(ArgumentMatchers.any())).thenReturn(Mono.just(topic));
        Mockito.when(cpfValidationService.isAbleToVote(ArgumentMatchers.any())).thenReturn(Mono.just(true));
        Mockito.when(
                voteService.receiveVote(
                        ArgumentMatchers.eq(1L),
                        ArgumentMatchers.eq(1L),
                        ArgumentMatchers.eq("Sim")
                ))
                .thenReturn(Mono.just(vote));

        VoteRequest request = new VoteRequest();
        request.setAssociateId(1L);
        request.setTopicName("Tópico");
        request.setVote("Sim");

        client.post()
                .uri("/vote/receive")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.associateId").isEqualTo(1L)
                .jsonPath("$.topicId").isEqualTo(1L)
                .jsonPath("$.topicName").isEqualTo("Tópico")
                .jsonPath("$.voteValue").isEqualTo("Sim");

        Mockito.verify(associateService, Mockito.times(1))
                .retrieveAssociate(ArgumentMatchers.eq(1L));
        Mockito.verifyNoMoreInteractions(associateService);

        Mockito.verify(topicService, Mockito.times(1))
                .findByName(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(topicService);

        Mockito.verify(cpfValidationService, Mockito.times(1))
                .isAbleToVote(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(cpfValidationService);

        Mockito.verify(voteService, Mockito.times(1))
                .receiveVote(
                        ArgumentMatchers.eq(1L),
                        ArgumentMatchers.eq(1L),
                        ArgumentMatchers.eq("Sim")
                );
        Mockito.verifyNoMoreInteractions(voteService);
    }

    @Test
    void testCountVotes() {
        CountVoteResponse response = new CountVoteResponse();
        response.setYes(1L);
        response.setNo(0L);

        Mockito.when(voteService.countVotes(ArgumentMatchers.eq(1L))).thenReturn(Mono.just(response));

        client.get()
                .uri("/vote/count/{id}", 1L)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.yes").isEqualTo(1L)
                .jsonPath("$.no").isEqualTo(0L);

        Mockito.verify(voteService, Mockito.times(1)).countVotes(ArgumentMatchers.eq(1L));
        Mockito.verifyNoMoreInteractions(voteService);
    }

}
