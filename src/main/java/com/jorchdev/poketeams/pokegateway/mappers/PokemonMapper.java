package com.jorchdev.poketeams.pokegateway.mappers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.PokemonResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.TeamPokemon;
import org.springframework.stereotype.Component;

@Component
public class PokemonMapper {
    public PokemonResponseDto toDto (TeamPokemon teamPokemon){
        return new PokemonResponseDto(
                teamPokemon.getId(),
                teamPokemon.getPokemonId(),
                teamPokemon.getName(),
                teamPokemon.getPokemonName(),
                teamPokemon.getTypes(),
                teamPokemon.getMoves());
    }
}
