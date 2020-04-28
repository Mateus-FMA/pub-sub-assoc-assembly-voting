package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.components.manager.VoteSessionManager;
import com.mateusfma.assemblyvoting.controller.rest.request.CreateTopicRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.TopicResponse;
import com.mateusfma.assemblyvoting.entity.Topic;
import com.mateusfma.assemblyvoting.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    public Mono<Topic> findByName(Mono<String> topicName) {
        return topicName.flatMap(name -> repository.findByName(name));
    }

    @Override
    public Mono<TopicResponse> openVoteSession(Mono<Topic> topic) {
        return topic
                .flatMap(t -> {
                    sessionManager.startSession(
                            t.getDurationSec(),
                            Mono.defer(() -> {
                                t.setOpen(false);
                                return repository.save(t);
                            })
                    );

                    return repository.save(t);
                })
                .map(t -> {
                    TopicResponse response = new TopicResponse();
                    response.setId(t.getId());
                    response.setName(t.getName());
                    response.setOpen(t.getOpen());
                    response.setStart(Date.from(t.getStart().toInstant()));
                    response.setDurationSec(t.getDurationSec());

                    return response;
                });
    }
}
