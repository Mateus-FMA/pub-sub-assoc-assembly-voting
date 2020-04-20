package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.exceptions.ClosedTopicException;
import com.mateusfma.assemblyvoting.exceptions.InvalidVoteException;
import com.mateusfma.assemblyvoting.repository.VoteRepository;
import com.mateusfma.assemblyvoting.router.rest.enums.VoteValue;
import com.mateusfma.assemblyvoting.router.rest.request.CountVoteRequest;
import com.mateusfma.assemblyvoting.router.rest.request.VoteRequest;
import com.mateusfma.assemblyvoting.router.rest.response.CountVoteResponse;
import com.mateusfma.assemblyvoting.router.rest.response.VoteResponse;
import com.mateusfma.assemblyvoting.entity.Associate;
import com.mateusfma.assemblyvoting.entity.Topic;
import com.mateusfma.assemblyvoting.entity.Vote;
import com.mateusfma.assemblyvoting.repository.AssociateRepository;
import com.mateusfma.assemblyvoting.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private AssociateRepository associateRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Mono<VoteResponse> receiveVote(Mono<VoteRequest> request) {
        return request
                .flatMap(req -> Mono.zip(
                        request,
                        associateRepository.findById(req.getAssociateId()),
                        topicRepository.findByName(req.getTopicName())))
                .flatMap(objs -> {
                    VoteRequest req = objs.getT1();
                    Associate associate = objs.getT2();
                    Topic topic = objs.getT3();

                    if (topic.getStart() == null || topic.getDurationSec() == null)
                        throw new ClosedTopicException("Pauta não se encontra aberta para votação.");

                    Instant start = topic.getStart().toInstant();
                    Instant end = start.plusSeconds(topic.getDurationSec());

                    if (!topic.getOpen() || end.isBefore(Instant.now()))
                        throw new ClosedTopicException("Pauta não se encontra aberta ou foi encerrada.");

                    if (VoteValue.fromValue(req.getVote()) == null)
                        throw new InvalidVoteException("Valor do voto é inválido: \"" + req.getVote() + "\".");

                    Vote vote = new Vote();
                    vote.setAssociateId(associate.getId());
                    vote.setTopicId(topic.getId());
                    vote.setValue(VoteValue.fromValue(req.getVote()).getValue());

                    return Mono.zip(Mono.just(topic.getName()), voteRepository.save(vote));
                })
                .map(objs -> {
                    String topicName = objs.getT1();
                    Vote vote = objs.getT2();

                    VoteResponse response = new VoteResponse();
                    response.setAssociateId(vote.getAssociateId());
                    response.setTopicId(vote.getTopicId());
                    response.setTopicName(topicName);
                    response.setVoteValue(vote.getValue() ? "Sim" : "Não");

                    return response;
                });
    }

    @Override
    public Mono<CountVoteResponse> countVotes(Mono<CountVoteRequest> request) {
        return request
                .flatMap(req -> topicRepository.findByName(req.getTopicName()))
                .flatMap(topic -> Mono.zip(
                        voteRepository.countVotes(topic.getId(), VoteValue.YES.getValue()),
                        voteRepository.countVotes(topic.getId(), VoteValue.NO.getValue())))
                .map(objs -> {
                    CountVoteResponse response = new CountVoteResponse();
                    response.setYes(objs.getT1());
                    response.setNo(objs.getT2());

                    return response;
                });
    }
}
