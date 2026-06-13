package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.client.PokemonServiceClient;
import com.jorchdev.poketeams.pokegateway.dtos.responses.PokemonResponseDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PokemonController {
    private final PokemonServiceClient pokemonServiceClient;

    public PokemonController(PokemonServiceClient pokemonServiceClient) {
        this.pokemonServiceClient = pokemonServiceClient;
    }

    @QueryMapping
    public PokemonResponseDto findPokemonById(@Argument int id){
        return pokemonServiceClient.getPokemonById(id);
    }

    @QueryMapping
    public PokemonResponseDto findPokemonByName(@Argument String name){
        return pokemonServiceClient.getPokemonByName(name);
    }
}
