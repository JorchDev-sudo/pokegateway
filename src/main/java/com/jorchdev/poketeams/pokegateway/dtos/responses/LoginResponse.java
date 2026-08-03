package com.jorchdev.poketeams.pokegateway.dtos.responses;

public record LoginResponse (
        TrainerResponse trainerResponse,
        String token
) { }
