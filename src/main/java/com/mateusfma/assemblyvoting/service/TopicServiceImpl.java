package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.components.manager.VoteSessionManager;
import com.mateusfma.assemblyvoting.exceptions.IllegalTopicStateException;
import com.mateusfma.assemblyvoting.router.rest.request.CreateTopicRequest;
import com.mateusfma.assemblyvoting.router.rest.request.OpenVoteSessionRequest;
import com.mateusfma.assemblyvoting.router.rest.response.TopicResponse;
import com.mateusfma.assemblyvoting.entity.Topic;
import com.mateusfma.assemblyvoting.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Date;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository repository;

    @Autowired
    private VoteSessionManager sessionManager;

    @Override
    public Mono<TopicResponse> createTopic(Mono<CreateTopicRequest> request) {
        return request
                .flatMap(req -> {
                    Topic topic = new Topic();
                    topic.setName(req.getName());
                    topic.setOpen(false);

                    return repository.save(topic);
                })
                .map(topic -> {
                    TopicResponse response = new TopicResponse();
                    response.setId(topic.getId());
                    response.setName(topic.getName());
                    response.setOpen(topic.getOpen());

                    return response;
                });
    }

    @Override
    public Mono<TopicResponse> openVoteSession(Mono<OpenVoteSessionRequest> request) {
        return request
                .flatMap(req -> Mono.zip(request, repository.findByName(req.getTopicName())))
                .flatMap(tuple -> {
                    OpenVoteSessionRequest req = tuple.getT1();
                    Topic topic = tuple.getT2();

                    if (topic.getStart() != null)
                        throw new IllegalTopicStateException("Pauta já foi iniciada em " +
                                topic.getStart().toString() + ".");

                    Integer durationSec = req.getDurationSec() == null ? 60 : req.getDurationSec();
                    topic.setStart(OffsetDateTime.now());
                    topic.setDurationSec(durationSec);
                    topic.setOpen(true);

                    sessionManager.startSession(topic.getDurationSec(), Mono.defer(() -> {
                                topic.setOpen(false);
                                return repository.save(topic);
                            }));

                    return repository.save(topic);
                })
                .map(topic -> {
                    TopicResponse response = new TopicResponse();
                    response.setId(topic.getId());
                    response.setName(topic.getName());
                    response.setOpen(topic.getOpen());
                    response.setStart(Date.from(topic.getStart().toInstant()));
                    response.setDurationSec(topic.getDurationSec());

                    return response;
                });
    }
}
