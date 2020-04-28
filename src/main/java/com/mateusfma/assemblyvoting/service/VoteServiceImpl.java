package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.controller.rest.enums.VoteValue;
import com.mateusfma.assemblyvoting.controller.rest.response.CountVoteResponse;
import com.mateusfma.assemblyvoting.entity.Vote;
import com.mateusfma.assemblyvoting.repository.AssociateRepository;
import com.mateusfma.assemblyvoting.repository.TopicRepository;
import com.mateusfma.assemblyvoting.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    public Mono<CountVoteResponse> countVotes(Long topicId) {
        return topicRepository.findById(topicId)
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
