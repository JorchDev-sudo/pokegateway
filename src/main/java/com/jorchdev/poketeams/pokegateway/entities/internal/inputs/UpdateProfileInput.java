package com.jorchdev.poketeams.pokegateway.entities.internal.inputs;

public record UpdateProfileInput(
        String name,
        String email
) {}