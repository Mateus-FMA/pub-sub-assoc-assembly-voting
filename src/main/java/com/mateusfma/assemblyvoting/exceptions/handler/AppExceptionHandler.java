package com.mateusfma.assemblyvoting.exceptions.handler;

import com.mateusfma.assemblyvoting.controller.rest.response.ErrorResponse;
import com.mateusfma.assemblyvoting.exceptions.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Mono;

import java.util.Date;

public class AppExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Mono<ErrorResponse>> handleAnyException(Exception e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(new Date());
        response.setMessage(e.getLocalizedMessage() == null ? e.toString() : e.getLocalizedMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(response));
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    public ResponseEntity<Mono<ErrorResponse>> handleInvalidRequestException(Exception e, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(new Date());
        response.setMessage(e.getLocalizedMessage() == null ? e.toString() : e.getLocalizedMessage());

        return ResponseEntity
                .badRequest()
                .body(Mono.just(response));
    }
}
