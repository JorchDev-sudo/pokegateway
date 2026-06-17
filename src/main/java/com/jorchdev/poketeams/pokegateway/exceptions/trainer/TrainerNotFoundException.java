package com.jorchdev.poketeams.pokegateway.exceptions.trainer;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(String message) {
        super(message);
    }
}
