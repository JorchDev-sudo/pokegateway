package com.jorchdev.poketeams.pokegateway.exceptions;

public class TeamFullException extends RuntimeException {
    public TeamFullException(String message) {
        super(message);
    }
}
