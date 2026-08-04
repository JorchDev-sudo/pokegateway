package com.jorchdev.poketeams.pokegateway.dtos.responses.internal;

import java.util.List;

public record ApiPokemonResponse(
        int id,
        String name,
        List<String> types,
        List<String> moves
){}
