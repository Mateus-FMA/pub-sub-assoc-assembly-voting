package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.controller.rest.enums.VoteValue;
import com.mateusfma.assemblyvoting.controller.rest.request.CountVoteRequest;
import com.mateusfma.assemblyvoting.controller.rest.request.VoteRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.CountVoteResponse;
import com.mateusfma.assemblyvoting.controller.rest.response.VoteResponse;
import com.mateusfma.assemblyvoting.entity.Associate;
import com.mateusfma.assemblyvoting.entity.Topic;
import com.mateusfma.assemblyvoting.entity.Vote;
import com.mateusfma.assemblyvoting.exceptions.ClosedTopicException;
import com.mateusfma.assemblyvoting.exceptions.InvalidVoteException;
import com.mateusfma.assemblyvoting.repository.AssociateRepository;
import com.mateusfma.assemblyvoting.repository.TopicRepository;
import com.mateusfma.assemblyvoting.repository.VoteRepository;
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
    public Mono<Vote> receiveVote(Long associateId, Long topicId, String value) {
        Vote vote = new Vote();
        vote.setAssociateId(associateId);
        vote.setTopicId(topicId);
        vote.setValue(VoteValue.fromValue(value).getValue());

        return voteRepository.save(vote);
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
