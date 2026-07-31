package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.client.PokemonServiceClient;
import com.jorchdev.poketeams.pokegateway.dtos.responses.PokemonResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.services.helpers.TrainerHelper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.UUID;

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
    public PokemonResponseDto findPokemonById(@Argument int id){
        return pokemonServiceClient.getPokemonById(id);
    }

    @QueryMapping
    public PokemonResponseDto findPokemonByName(@Argument String name){
        return pokemonServiceClient.getPokemonByName(name);
    }

    @MutationMapping
    public PokemonResponseDto changePokemonName(UUID id, String name, Authentication auth){
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);


    }
}
