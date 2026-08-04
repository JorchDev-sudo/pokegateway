package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.client.PokemonServiceClient;
import com.jorchdev.poketeams.pokegateway.dtos.responses.TeamResponse;
import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.PokemonSyncInput;
import com.jorchdev.poketeams.pokegateway.exceptions.pokemon.PokemonException;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerException;
import com.jorchdev.poketeams.pokegateway.mappers.TeamMapper;
import com.jorchdev.poketeams.pokegateway.repositories.TeamRepository;
import com.jorchdev.poketeams.pokegateway.services.helpers.TeamHelper;
import com.jorchdev.poketeams.pokegateway.services.helpers.TrainerHelper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamHelper helper;
    private final TeamMapper mapper;

    private final TrainerHelper trainerHelper;

    private final PokemonServiceClient pokemonServiceClient;

    public TeamService(
            TeamRepository teamRepository,
            TeamHelper teamHelper,
            TeamMapper teamMapper,
            TrainerHelper trainerHelper,
            PokemonServiceClient pokemonServiceClient)
    {
        this.teamRepository = teamRepository;
        this.helper = teamHelper;
        this.mapper = teamMapper;
        this.trainerHelper = trainerHelper;
        this.pokemonServiceClient = pokemonServiceClient;
    }

    public TeamResponse createTeam(UUID trainerId, String name) throws TrainerException {
        Trainer trainer = trainerHelper.getTrainerById(trainerId);

        if (trainer.getTeam() != null) {
            throw new TrainerException("You can only have a team");
        }

        Team team = mapper.toEntity(name, trainer);
        Team savedTeam = teamRepository.save(team);

        trainer.setTeam(savedTeam);

        return mapper.toDto(savedTeam);
    }

    public TeamResponse findTeamById(UUID teamId) throws EntityNotFoundException {
        Team team = helper.getTeamById(teamId);

        TeamResponse dto = mapper.toDto(team);

        System.out.println(dto);

        return dto;
    }

    public List<TeamResponse> findAllTeams() {
        List<Team> teams = teamRepository.findAll();

        List<TeamResponse> response = new ArrayList<>();

        for (Team team : teams) {
            response.add(mapper.toDto(team));
        }

        return response;
    }

    @Transactional
    public TeamResponse syncPokemons(List<PokemonSyncInput> pokemons, UUID teamId) {
        Team team = helper.getTeamById(teamId);

        //Todo Add more pokemon validations
        for  (PokemonSyncInput pokemon : pokemons) {
            if (pokemonServiceClient.getPokemonById(pokemon.pokemonId).id() == 0) {
                throw new PokemonException("Pokemon not found");
            }
        }

        team.syncPokemons(pokemons);

        Team savedTeam = teamRepository.save(team);

        return mapper.toDto(savedTeam);
    }
}
