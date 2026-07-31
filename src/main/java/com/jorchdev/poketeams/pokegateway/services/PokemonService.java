package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.dtos.responses.PokemonResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.entities.TeamPokemon;
import com.jorchdev.poketeams.pokegateway.mappers.PokemonMapper;
import com.jorchdev.poketeams.pokegateway.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PokemonService {
    private final PokemonMapper pokemonMapper;

    public PokemonService (PokemonMapper pokemonMapper)
    {
        this.pokemonMapper = pokemonMapper;
    }

    public PokemonResponseDto changePokemonName (Team team, UUID id, String newName){
        for (TeamPokemon pokemon : team.getPokemons()){
            if (pokemon.getId().equals(id)){
                pokemon.setPokemonName(newName);

                return pokemonMapper.toDto(pokemon);
            }
        }

        return null;
    }
}
