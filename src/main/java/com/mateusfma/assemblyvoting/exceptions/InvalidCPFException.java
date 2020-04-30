package com.mateusfma.assemblyvoting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCPFException extends RuntimeException {

    public InvalidCPFException(String message) {
        super(message);
    }
}
