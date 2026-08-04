package com.jorchdev.poketeams.pokegateway.entities.internal.inputs;

import java.util.UUID;

public class PokemonSyncInput {
    public UUID id;
    public final int pokemonId;
    public final String pokemonName;
    public String nickname;
    public final int position;

    public PokemonSyncInput(UUID id, int pokemonId, String pokemonName, String nickname, int position) {
        this.id = id;
        this.pokemonId = pokemonId;
        this.pokemonName = pokemonName;
        this.nickname = nickname;
        this.position = position;
    }
}