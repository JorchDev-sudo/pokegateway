package com.jorchdev.poketeams.pokegateway.exceptions.team;

public class PokemonNotFoundInTeamException extends RuntimeException {
    public PokemonNotFoundInTeamException(String message) {
        super(message);
    }
}
