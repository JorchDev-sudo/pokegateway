package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.client.PokemonServiceClient;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.ApiPokemonResponse;
import com.jorchdev.poketeams.pokegateway.dtos.responses.TeamResponse;
import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.PokemonPositionInput;
import com.jorchdev.poketeams.pokegateway.exceptions.team.PokemonNotFoundInTeamException;
import com.jorchdev.poketeams.pokegateway.exceptions.team.TeamException;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerAlreadyHaveATeamException;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerNotFoundException;
import com.jorchdev.poketeams.pokegateway.mappers.TeamMapper;
import com.jorchdev.poketeams.pokegateway.repositories.TeamRepository;
import com.jorchdev.poketeams.pokegateway.services.helpers.TeamHelper;
import com.jorchdev.poketeams.pokegateway.services.helpers.TrainerHelper;
import jakarta.persistence.EntityNotFoundException;
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

    public TeamResponse createTeam(UUID trainerId, String name) throws TrainerNotFoundException {
        Trainer trainer = trainerHelper.getTrainerById(trainerId);

        if (trainer.getTeam() != null) {
            throw new TrainerAlreadyHaveATeamException("You can only have a team");
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

    public TeamResponse addPokemonToTeamById(int pokemonId, UUID teamId) throws TeamException, EntityNotFoundException {
        Team team = helper.getTeamById(teamId);

        ApiPokemonResponse pokemon = pokemonServiceClient.getPokemonById(pokemonId);

        team.addPokemon(pokemon.id(), pokemon.name());
        Team savedTeam = teamRepository.save(team);

        return mapper.toDto(savedTeam);
    }

    public TeamResponse addPokemonToTeamByName(String name, UUID teamId) throws TeamException, EntityNotFoundException {
        //TODO Eliminar este helper y usar el repositorio directamente
        Team team = helper.getTeamById(teamId);

        ApiPokemonResponse pokemon = pokemonServiceClient.getPokemonByName(name);

        team.addPokemon(pokemon.id(), pokemon.name());
        Team savedTeam = teamRepository.save(team);

        return mapper.toDto(savedTeam);
    }

    public TeamResponse movePokemons(List<PokemonPositionInput> positions, UUID teamId) {
        Team team = helper.getTeamById(teamId);

        team.movePokemons(positions);

        Team savedTeam = teamRepository.save(team);

        return mapper.toDto(savedTeam);
    }

    public TeamResponse removePokemon(UUID id, UUID teamId) throws PokemonNotFoundInTeamException {
        Team team = helper.getTeamById(teamId);

        team.removePokemon(id);

        Team savedTeam = teamRepository.save(team);

        return mapper.toDto(savedTeam);
    }

}
