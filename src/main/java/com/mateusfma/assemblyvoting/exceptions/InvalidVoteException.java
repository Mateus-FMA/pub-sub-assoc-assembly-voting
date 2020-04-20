package com.mateusfma.assemblyvoting.exceptions;

public class InvalidVoteException extends RuntimeException {

    public InvalidVoteException(String message) {
        super(message);
    }

}
