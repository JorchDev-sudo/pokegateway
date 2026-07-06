package com.jorchdev.poketeams.pokegateway.dtos.responses.internal;

import java.util.List;
import java.util.UUID;

public record TeamSummary(UUID id, String name, List<TeamPokemonResponseDto> pokemons) {
}
