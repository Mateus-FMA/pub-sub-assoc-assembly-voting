package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.controller.rest.request.CreateTopicRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.TopicResponse;
import com.mateusfma.assemblyvoting.entity.Topic;
import reactor.core.publisher.Mono;

public interface TopicService {

    Mono<TopicResponse> createTopic(Mono<CreateTopicRequest> request);

    Mono<Topic> findByName(Mono<String> topicName);

    Mono<TopicResponse> openVoteSession(Mono<Topic> topic);

}
