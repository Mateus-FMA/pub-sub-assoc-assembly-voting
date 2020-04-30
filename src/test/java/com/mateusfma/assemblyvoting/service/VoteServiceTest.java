package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.entity.Vote;
import com.mateusfma.assemblyvoting.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    private VoteServiceImpl service;

    @Mock
    private VoteRepository repository;

    @Test
    void testReceiveVote() {
        Vote vote = new Vote();
        vote.setAssociateId(1L);
        vote.setTopicId(1L);
        vote.setValue(true);

        Mockito.when(repository.save(ArgumentMatchers.any(Vote.class))).thenReturn(Mono.just(vote));

        StepVerifier
                .create(service.receiveVote(1L, 1L, "Sim"))
                .expectNextMatches(vote1 -> Objects.equals(vote1.getAssociateId(), 1L)
                        && Objects.equals(vote1.getTopicId(), 1L)
                        && Objects.equals(vote1.getValue(), true)
                )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Vote.class));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testCountVotes() {
        Mockito.when(repository.countVotes(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(true)))
                .thenReturn(Mono.just(3L));
        Mockito.when(repository.countVotes(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(false)))
                .thenReturn(Mono.just(5L));

        StepVerifier
                .create(service.countVotes(1L))
                .expectNextMatches(response -> Objects.equals(response.getYes(), 3L)
                        && Objects.equals(response.getNo(), 5L)
                )
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .countVotes(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(true));

        Mockito.verify(repository, Mockito.times(1))
                .countVotes(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(false));

        Mockito.verifyNoMoreInteractions(repository);
    }
}
