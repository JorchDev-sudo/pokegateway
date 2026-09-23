package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.client.PokemonServiceClient;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.ApiPokemonResponse;
import com.jorchdev.poketeams.pokegateway.services.helpers.TrainerHelper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PokemonController {
    private final PokemonServiceClient pokemonServiceClient;
    private final TrainerHelper trainerHelper;

    public PokemonController(
            PokemonServiceClient pokemonServiceClient,
            TrainerHelper trainerHelper)
    {
        this.pokemonServiceClient = pokemonServiceClient;
        this.trainerHelper = trainerHelper;
    }

    @QueryMapping
    public ApiPokemonResponse findPokemonById(@Argument int id){
        return pokemonServiceClient.getPokemonById(id);
    }

    @QueryMapping
    public ApiPokemonResponse findPokemonByName(@Argument String name){
        return pokemonServiceClient.getPokemonByName(name);
    }
}
