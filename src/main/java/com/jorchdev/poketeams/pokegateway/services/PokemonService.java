package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.ApiPokemonResponse;
import com.jorchdev.poketeams.pokegateway.entities.Pokemon;
import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.mappers.PokemonMapper;
import com.jorchdev.poketeams.pokegateway.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PokemonService {
    private final PokemonMapper pokemonMapper;
    private final TeamRepository teamRepository;

    public PokemonService (PokemonMapper pokemonMapper,
                           TeamRepository teamRepository)
    {
        this.pokemonMapper = pokemonMapper;
        this.teamRepository = teamRepository;
    }

    //Todo Deprecate this
    public ApiPokemonResponse changePokemonNickname (Team team, UUID id, String newNickname){
        for (Pokemon pokemon : team.getPokemons()){
            if (pokemon.getId().equals(id)){
                pokemon.setNickname(newNickname);

                teamRepository.save(team);
                return pokemonMapper.toDto(pokemon);
            }
        }

        return null;
    }
}
