package com.jorchdev.poketeams.pokegateway.dtos.responses;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TeamPokemonResponseDto;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TrainerSummary;

import java.util.List;
import java.util.UUID;

public record TeamResponseDto(
        UUID id,
        String name,
        TrainerSummary trainer,
        List<TeamPokemonResponseDto> pokemons) {}
