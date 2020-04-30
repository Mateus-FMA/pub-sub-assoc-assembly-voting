package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.components.manager.VoteSessionManager;
import com.mateusfma.assemblyvoting.controller.rest.request.CreateTopicRequest;
import com.mateusfma.assemblyvoting.entity.Topic;
import com.mateusfma.assemblyvoting.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @InjectMocks
    private TopicServiceImpl service;

    @Mock
    private VoteSessionManager manager;

    @Mock
    private TopicRepository repository;

    @Test
    void testCreateTopic() {
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Tópico");
        topic.setOpen(false);

        Mockito.when(repository.save(ArgumentMatchers.any(Topic.class))).thenReturn(Mono.just(topic));

        CreateTopicRequest request = new CreateTopicRequest();
        request.setName("Tópico");

        StepVerifier
                .create(service.createTopic(Mono.just(request)))
                .expectNextMatches(response -> Objects.equals(response.getId(), 1L)
                        && Objects.equals(response.getName(), "Tópico")
                        && Objects.equals(response.getOpen(), false)
                        && Objects.equals(response.getStart(), null)
                        && Objects.equals(response.getDurationSec(), null)
                )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .save(ArgumentMatchers.any(Topic.class));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindByName() {
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Tópico");
        topic.setOpen(false);

        Mockito.when(repository.findByName(ArgumentMatchers.eq("Tópico"))).thenReturn(Mono.just(topic));

        StepVerifier
                .create(service.findByName(Mono.just("Tópico")))
                .expectNextMatches(response -> Objects.equals(response.getId(), 1L)
                        && Objects.equals(response.getName(), "Tópico")
                        && Objects.equals(response.getOpen(), false)
                        && Objects.equals(response.getStart(), null)
                        && Objects.equals(response.getDurationSec(), null)
                )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .findByName(ArgumentMatchers.eq("Tópico"));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testOpenVoteSession() {
        OffsetDateTime now = OffsetDateTime.now();

        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Tópico");
        topic.setOpen(true);
        topic.setDurationSec(60);
        topic.setStart(now);

        Mockito.when(repository.save(ArgumentMatchers.any(Topic.class))).thenReturn(Mono.just(topic));
        Mockito.doNothing()
                .when(manager)
                .startSession(ArgumentMatchers.anyInt(), ArgumentMatchers.any());

        StepVerifier
                .create(service.openVoteSession(Mono.just(topic)))
                .expectNextMatches(response -> Objects.equals(topic.getId(), 1L)
                        && Objects.equals(response.getName(), "Tópico")
                        && Objects.equals(response.getOpen(), true)
                        && Objects.equals(response.getDurationSec(), 60)
                        && Objects.equals(response.getStart(), Date.from(now.toInstant()))
                )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .save(ArgumentMatchers.any(Topic.class));
        Mockito.verify(manager, Mockito.times(1))
                .startSession(ArgumentMatchers.anyInt(), ArgumentMatchers.any());

        Mockito.verifyNoMoreInteractions(repository, manager);
    }
}
