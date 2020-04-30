package com.mateusfma.assemblyvoting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchAssociateException extends RuntimeException {

    public NoSuchAssociateException(String message) {
        super(message);
    }
}
