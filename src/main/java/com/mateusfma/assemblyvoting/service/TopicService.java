package com.mateusfma.assemblyvoting.service;

import com.mateusfma.assemblyvoting.router.rest.request.CreateTopicRequest;
import com.mateusfma.assemblyvoting.router.rest.request.OpenVoteSessionRequest;
import com.mateusfma.assemblyvoting.router.rest.response.TopicResponse;
import reactor.core.publisher.Mono;

public interface TopicService {

    Mono<TopicResponse> createTopic(Mono<CreateTopicRequest> request);

    Mono<TopicResponse> openVoteSession(Mono<OpenVoteSessionRequest> request);

}
