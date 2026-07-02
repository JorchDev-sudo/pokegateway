package com.jorchdev.poketeams.pokegateway.exceptions.trainer;

public class TrainerAlreadyHaveATeamException extends RuntimeException {
    public TrainerAlreadyHaveATeamException(String message) {
        super(message);
    }
}
