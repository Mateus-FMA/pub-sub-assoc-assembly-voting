package com.mateusfma.assemblyvoting.router;

import com.mateusfma.assemblyvoting.components.handler.CreateTopicRequestHandler;
import com.mateusfma.assemblyvoting.components.handler.OpenVoteSessionRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TopicRouter {

    @Bean
    public RouterFunction<ServerResponse> routeTopicAPI(
            CreateTopicRequestHandler createTopicRequestHandler,
            OpenVoteSessionRequestHandler openVoteSessionRequestHandler) {
        return RouterFunctions
                .route(
                        RequestPredicates
                                .POST("/topic/create")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        createTopicRequestHandler::handleRequest)
                .andRoute(
                        RequestPredicates
                                .POST("/topic/open")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        openVoteSessionRequestHandler::handleRequest);
    }
}
