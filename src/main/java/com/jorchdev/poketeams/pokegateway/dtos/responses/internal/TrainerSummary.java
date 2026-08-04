package com.jorchdev.poketeams.pokegateway.dtos.responses.internal;

import java.util.UUID;

public record TrainerSummary(
        UUID id,
        String name,
        String email
) {}
