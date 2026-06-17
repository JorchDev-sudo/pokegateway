package com.jorchdev.poketeams.pokegateway.exceptions.trainer;

public class DuplicateTrainerException extends RuntimeException {
    public DuplicateTrainerException(String message) {
        super(message);
    }
}
