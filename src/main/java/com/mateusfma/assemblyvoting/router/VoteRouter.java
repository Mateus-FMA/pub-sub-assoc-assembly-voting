package com.mateusfma.assemblyvoting.router;

import com.mateusfma.assemblyvoting.components.handler.CountVoteRequestHandler;
import com.mateusfma.assemblyvoting.components.handler.VoteRequestHandler;
import com.mateusfma.assemblyvoting.router.rest.request.VoteRequest;
import com.mateusfma.assemblyvoting.router.rest.response.VoteResponse;
import com.mateusfma.assemblyvoting.service.AssociateService;
import com.mateusfma.assemblyvoting.service.CPFValidationService;
import com.mateusfma.assemblyvoting.service.VoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class VoteRouter {

    @Bean
    public RouterFunction<ServerResponse> routeVoteAPI(
            VoteRequestHandler voteRequestHandler,
            CountVoteRequestHandler countVoteRequestHandler) {
        return RouterFunctions
                .route(
                        RequestPredicates
                                .POST("/vote/receive")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        voteRequestHandler::handleRequest)
                .andRoute(
                        RequestPredicates.GET("/vote/count")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        countVoteRequestHandler::handleRequest);
    }

}
