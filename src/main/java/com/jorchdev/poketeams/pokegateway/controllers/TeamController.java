package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TeamResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
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
    public TeamResponseDto findTeamById(@Argument UUID id){
        return service.findTeamById(id);
    }

    //Todo Añadir paginación a findAllTeams
    @QueryMapping
    public List<TeamResponseDto> findAllTeams(){
        return service.findAllTeams();
    }

    @MutationMapping
    public TeamResponseDto createTeam(@Argument String name, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        return service.createTeam(trainer.getId(), name);
    }

    @MutationMapping
    public TeamResponseDto addPokemonToTeam(@Argument int pokemonId, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        UUID teamId = trainer.getTeam().getId();

        return service.addPokemonToTeam(pokemonId, teamId);
    }

    @MutationMapping
    public TeamResponseDto  removePokemonFromTeam(@Argument int pokemonId, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        UUID teamId = trainer.getTeam().getId();

        return service.removePokemonFromTeam(pokemonId, teamId);
    }
}
