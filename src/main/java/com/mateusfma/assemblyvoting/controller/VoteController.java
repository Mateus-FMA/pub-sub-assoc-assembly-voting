package com.mateusfma.assemblyvoting.controller;

import com.mateusfma.assemblyvoting.controller.rest.enums.VoteValue;
import com.mateusfma.assemblyvoting.controller.rest.request.VoteRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.CountVoteResponse;
import com.mateusfma.assemblyvoting.controller.rest.response.VoteResponse;
import com.mateusfma.assemblyvoting.entity.Associate;
import com.mateusfma.assemblyvoting.entity.Topic;
import com.mateusfma.assemblyvoting.entity.Vote;
import com.mateusfma.assemblyvoting.exceptions.ClosedTopicException;
import com.mateusfma.assemblyvoting.exceptions.InvalidCPFException;
import com.mateusfma.assemblyvoting.exceptions.InvalidVoteException;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import com.mateusfma.assemblyvoting.service.TopicService;
import com.mateusfma.assemblyvoting.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private AssociateService associateService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private CPFValidationService cpfValidationService;

    @Autowired
    private VoteService voteService;

    @PostMapping(value = "/receive")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<VoteResponse> receiveVote(@RequestBody VoteRequest request) {
        return Mono.zip(
                    associateService.retrieveAssociate(request.getAssociateId()),
                    topicService.findByName(Mono.just(request.getTopicName())))
                .flatMap(tuple -> {
                    Associate associate = tuple.getT1();
                    Topic topic = tuple.getT2();

                    return Mono.zip(
                            cpfValidationService.isAbleToVote(Mono.just(associate.getCpf())),
                            Mono.just(associate),
                            Mono.just(topic)
                    );
                })
                .flatMap(tuple -> {
                    Boolean able = tuple.getT1();
                    Associate associate = tuple.getT2();
                    Topic topic = tuple.getT3();

                    if (!able)
                        throw new InvalidCPFException("CPF inválido.");

                    if (topic.getStart() == null || topic.getDurationSec() == null)
                        throw new ClosedTopicException("Pauta não se encontra aberta para votação.");

                    Instant start = topic.getStart().toInstant();
                    Instant end = start.plusSeconds(topic.getDurationSec());

                    if (!topic.getOpen() || end.isBefore(Instant.now()))
                        throw new ClosedTopicException("Pauta não se encontra aberta ou foi encerrada.");

                    if (VoteValue.fromValue(request.getVote()) == null)
                        throw new InvalidVoteException("Valor do voto é inválido: \"" + request.getVote() + "\".");


                    return Mono.zip(
                            voteService.receiveVote(associate.getId(), topic.getId(), request.getVote()),
                            Mono.just(request.getTopicName())
                    );
                })
                .map(tuple -> {
                    Vote vote = tuple.getT1();
                    String topicName = tuple.getT2();

                    VoteResponse voteResponse = new VoteResponse();
                    voteResponse.setAssociateId(vote.getAssociateId());
                    voteResponse.setTopicId(vote.getTopicId());
                    voteResponse.setTopicName(topicName);
                    voteResponse.setVoteValue(vote.getValue() ? "Sim" : "Não");

                    return voteResponse;
                });
    }

    @GetMapping(value = "/count/{topicId}")
    public ResponseEntity<Mono<CountVoteResponse>> countVotes(@PathVariable Long topicId) {
        return ResponseEntity.ok(voteService.countVotes(topicId));
    }
}
