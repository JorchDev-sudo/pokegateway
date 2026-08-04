package com.jorchdev.poketeams.pokegateway.dtos.responses.internal;

import java.util.UUID;

public record InnerPokemonResponse(UUID id, int pokemonId, String pokemonName, String nickname, int position) {}
