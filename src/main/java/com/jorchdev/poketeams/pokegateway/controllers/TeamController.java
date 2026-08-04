package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TeamResponse;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.PokemonSyncInput;
import com.jorchdev.poketeams.pokegateway.services.TeamService;
import com.jorchdev.poketeams.pokegateway.services.helpers.TrainerHelper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class TeamController {
    private final TeamService service;
    private final TrainerHelper trainerHelper;

    public TeamController(
            TeamService teamService,
            TrainerHelper trainerHelper)
    {
        this.service = teamService;
        this.trainerHelper = trainerHelper;
    }

    @QueryMapping
    public TeamResponse findTeamById(@Argument UUID id){
        return service.findTeamById(id);
    }

    //Todo Add pagination
    @QueryMapping
    public List<TeamResponse> findAllTeams(){
        return service.findAllTeams();
    }

    @MutationMapping
    public TeamResponse createTeam(@Argument String name, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        return service.createTeam(trainer.getId(), name);
    }

    @MutationMapping
    public TeamResponse syncPokemons(@Argument List<PokemonSyncInput> pokemons, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        UUID teamId = trainer.getTeam().getId();

        return service.syncPokemons(pokemons, teamId);
    }
}
