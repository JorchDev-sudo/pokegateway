package com.jorchdev.poketeams.pokegateway.exceptions.team;

public class TeamFullException extends RuntimeException {
    public TeamFullException(String message) {
        super(message);
    }
}
