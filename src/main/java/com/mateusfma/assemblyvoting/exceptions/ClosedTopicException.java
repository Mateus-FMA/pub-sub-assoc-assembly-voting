package com.mateusfma.assemblyvoting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClosedTopicException extends RuntimeException {

    public ClosedTopicException(String message) {
        super(message);
    }
}
