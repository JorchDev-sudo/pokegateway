package com.jorchdev.poketeams.pokegateway.dtos.responses;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TeamSummary;

import java.util.UUID;

public record TrainerResponse (
        UUID id,
        String name,
        String email,
        TeamSummary team) { }
