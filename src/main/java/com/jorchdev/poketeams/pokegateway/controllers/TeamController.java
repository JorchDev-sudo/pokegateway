package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TeamResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.PokemonPositionInput;
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
    public TeamResponseDto addPokemonToTeamById(@Argument int pokemonId, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        UUID teamId = trainer.getTeam().getId();

        return service.addPokemonToTeamById(pokemonId, teamId);
    }

    @MutationMapping
    public TeamResponseDto addPokemonToTeamByName(@Argument String pokemonName, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        UUID teamId = trainer.getTeam().getId();

        return service.addPokemonToTeamByName(pokemonName, teamId);
    }

    @MutationMapping
    public TeamResponseDto movePokemons(@Argument List<PokemonPositionInput> positions, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        UUID teamId = trainer.getTeam().getId();

        return service.movePokemons(positions, teamId);
    }

    @MutationMapping
    public TeamResponseDto removePokemonFromTeamById(@Argument UUID id, Authentication auth) {
        Trainer trainer = trainerHelper.getCurrentTrainer(auth);

        UUID teamId = trainer.getTeam().getId();

        return service.removePokemon(id, teamId);
    }
}
