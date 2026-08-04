package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.client.PokemonServiceClient;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.ApiPokemonResponse;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.services.PokemonService;
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
    private final PokemonService pokemonService;
    private final TrainerHelper trainerHelper;

    public PokemonController(
            PokemonServiceClient pokemonServiceClient,
            PokemonService pokemonService,
            TrainerHelper trainerHelper)
    {
        this.pokemonServiceClient = pokemonServiceClient;
        this.pokemonService = pokemonService;
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

    /*
    Todo Deprecate this
    @MutationMapping
    public ApiPokemonResponse changePokemonName(UUID id, String name, Authentication auth){
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        return pokemonService.changePokemonNickname(trainer.getTeam(),  id, name);
    }
     */
}
