package com.jorchdev.poketeams.pokegateway.mappers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.ApiPokemonResponse;
import com.jorchdev.poketeams.pokegateway.entities.Pokemon;
import org.springframework.stereotype.Component;

@Component
public class PokemonMapper {
    public ApiPokemonResponse toDto (Pokemon pokemon){
        return new ApiPokemonResponse(
                pokemon.getPokemonId(),
                pokemon.getName(),
                pokemon.getTypes(),
                pokemon.getMoves());
    }
}
