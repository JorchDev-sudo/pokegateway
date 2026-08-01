package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.dtos.responses.PokemonResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Pokemon;
import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.mappers.PokemonMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PokemonService {
    private final PokemonMapper pokemonMapper;

    public PokemonService (PokemonMapper pokemonMapper)
    {
        this.pokemonMapper = pokemonMapper;
    }

    public PokemonResponseDto changePokemonNickname (Team team, UUID id, String newNickname){
        for (Pokemon pokemon : team.getPokemons()){
            if (pokemon.getId().equals(id)){
                pokemon.setNickname(newNickname);

                return pokemonMapper.toDto(pokemon);
            }
        }

        return null;
    }
}
