package com.jorchdev.poketeams.pokegateway.mappers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.PokemonResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Pokemon;
import org.springframework.stereotype.Component;

@Component
public class PokemonMapper {
    public PokemonResponseDto toDto (Pokemon pokemon){
        return new PokemonResponseDto(
                pokemon.getId(),
                pokemon.getPokemonId(),
                pokemon.getName(),
                pokemon.getNickname(),
                pokemon.getTypes(),
                pokemon.getMoves());
    }
}
