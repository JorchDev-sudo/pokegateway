package com.jorchdev.poketeams.pokegateway.dtos.responses;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.InnerPokemonResponse;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TrainerSummary;

import java.util.List;
import java.util.UUID;

public record TeamResponse(
        UUID id,
        String name,
        TrainerSummary trainer,
        List<InnerPokemonResponse> pokemons) {}
