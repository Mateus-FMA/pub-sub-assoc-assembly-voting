package com.mateusfma.assemblyvoting.router;

import com.mateusfma.assemblyvoting.components.handler.AssociateRequestHandler;
import com.mateusfma.assemblyvoting.components.handler.AssociateUpdateRequestHandler;
import com.mateusfma.assemblyvoting.router.rest.response.AssociateResponse;
import com.mateusfma.assemblyvoting.service.AssociateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class AssociateRouter {

    @Bean
    public RouterFunction<ServerResponse> routeAssociateAPI(
            AssociateRequestHandler associateRequestHandler,
            AssociateUpdateRequestHandler associateUpdateRequestHandler,
            AssociateService associateService) {
        return RouterFunctions
                .route(
                        RequestPredicates
                                .POST("/associate/create")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        associateRequestHandler::handleRequest)
                .andRoute(
                        RequestPredicates
                                .GET("/associate/retrieve/{id}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        request -> {
                            Long id = Long.valueOf(request.pathVariable("id"));
                            return associateService.hasAssociate(id)
                                    .flatMap(exists -> {
                                        if (exists) {
                                            return associateService
                                                    .retrieveAssociate(id)
                                                    .flatMap(response -> ServerResponse
                                                            .ok()
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .body(Mono.just(response), AssociateResponse.class))
                                                    .onErrorResume(t -> ServerResponse
                                                            .badRequest()
                                                            .bodyValue("Erro ao buscar associado: " +
                                                                    t.getMessage()));
                                        }

                                        return ServerResponse.notFound().build();
                                    });
                        })
                .andRoute(
                        RequestPredicates
                                .PUT("/associate/update")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        associateUpdateRequestHandler::handleRequest)
                .andRoute(
                        RequestPredicates
                                .DELETE("/associate/delete/{id}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        request -> {
                            Long id = Long.valueOf(request.pathVariable("id"));
                            return associateService.hasAssociate(id)
                                    .flatMap(exists -> {
                                        if (exists) {
                                            return associateService
                                                    .deleteAssociate(id)
                                                    .flatMap(response -> ServerResponse
                                                            .ok()
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .body(Mono.just(response), AssociateResponse.class))
                                                    .onErrorResume(t -> ServerResponse
                                                            .badRequest()
                                                            .bodyValue("Não foi possível remover o associado: " +
                                                                    t.getMessage()));
                                        }

                                        return ServerResponse.notFound().build();
                                    });
                        });
    }
}
