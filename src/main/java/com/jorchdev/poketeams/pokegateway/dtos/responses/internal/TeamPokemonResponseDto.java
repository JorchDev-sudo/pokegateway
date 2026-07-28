package com.jorchdev.poketeams.pokegateway.dtos.responses.internal;

import java.util.UUID;

public record TeamPokemonResponseDto(UUID id, int pokemonId, String name, int position) {}
