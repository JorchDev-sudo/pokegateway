package com.jorchdev.poketeams.pokegateway.dtos.responses;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TeamPokemonResponseDto;

import java.util.List;
import java.util.UUID;

public record TeamResponseDto(
        UUID id,
        String name,
        TrainerResponseDto trainer,
        List<TeamPokemonResponseDto> pokemons) {}
