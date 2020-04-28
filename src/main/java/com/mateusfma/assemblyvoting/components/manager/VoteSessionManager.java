package com.mateusfma.assemblyvoting.components.manager;

import com.mateusfma.assemblyvoting.entity.Topic;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Component
public class VoteSessionManager {

    private Set<Disposable> disposables;

    public VoteSessionManager() {
        this.disposables = new HashSet<>();
    }

    public void startSession(Integer sessionDuration, Mono<Topic> onClose) {
        disposables.add(Flux.interval(Duration.ofSeconds(sessionDuration.longValue()))
                .takeUntil(sec -> sec < sessionDuration)
                .concatMap(timeElapsed -> onClose)
                .subscribeOn(Schedulers.parallel())
                .subscribe());
    }

    @PreDestroy
    public void clear() {
        disposables.forEach(Disposable::dispose);
    }

}
