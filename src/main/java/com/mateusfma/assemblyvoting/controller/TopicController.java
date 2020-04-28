package com.mateusfma.assemblyvoting.controller;

import com.mateusfma.assemblyvoting.controller.rest.request.CreateTopicRequest;
import com.mateusfma.assemblyvoting.controller.rest.request.OpenVoteSessionRequest;
import com.mateusfma.assemblyvoting.controller.rest.response.TopicResponse;
import com.mateusfma.assemblyvoting.exceptions.IllegalTopicStateException;
import com.mateusfma.assemblyvoting.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService service;

    @PostMapping(value = "/create")
    public ResponseEntity<Mono<TopicResponse>> createTopic(@RequestBody CreateTopicRequest request) {
        return ResponseEntity.ok(service.createTopic(Mono.just(request)));
    }

    @PostMapping(value = "/open")
    public ResponseEntity<Mono<TopicResponse>> openVoteSession(@RequestBody OpenVoteSessionRequest request) {
        Mono<TopicResponse> response = service.findByName(Mono.just(request.getTopicName()))
                .flatMap(topic -> {
                    if (topic.getStart() != null)
                        throw new IllegalTopicStateException("Pauta já foi iniciada em " +
                                topic.getStart().toString() + ".");

                    Integer durationSec = request.getDurationSec() == null ? 60 : request.getDurationSec();
                    topic.setStart(OffsetDateTime.now());
                    topic.setDurationSec(durationSec);
                    topic.setOpen(true);

                    return service.openVoteSession(Mono.just(topic));
                });

        return ResponseEntity.ok(response);
    }

}
