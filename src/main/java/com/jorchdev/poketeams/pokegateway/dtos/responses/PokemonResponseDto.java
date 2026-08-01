package com.jorchdev.poketeams.pokegateway.dtos.responses;

import java.util.List;
import java.util.UUID;

public record PokemonResponseDto (
        UUID id,
        int pokemonId,
        String name,
        String nickname,
        List<String> types,
        List<String> moves
){}
