package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.client.PokemonServiceClient;
import com.jorchdev.poketeams.pokegateway.dtos.responses.PokemonResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.exceptions.team.TeamFullException;
import com.jorchdev.poketeams.pokegateway.repositories.TeamRepository;
import com.jorchdev.poketeams.pokegateway.services.helpers.TrainerHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TrainerHelper trainerHelper;

    private final PokemonServiceClient pokemonServiceClient;

    public TeamService(
            TeamRepository teamRepository,
            TrainerHelper trainerHelper,
            PokemonServiceClient pokemonServiceClient)
    {
        this.teamRepository = teamRepository;
        this.trainerHelper = trainerHelper;
        this.pokemonServiceClient = pokemonServiceClient;
    }

    public Team createTeam(UUID trainerId, String name) throws EntityNotFoundException {
        Trainer trainer = trainerHelper.getTrainerById(trainerId);

        Team team = new Team();
        team.setName(name);
        team.setTrainer(trainer);

        return teamRepository.save(team);
    }

    public Team findTeamById(UUID teamId) throws EntityNotFoundException {
        return teamRepository.findById(teamId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Team addPokemonToTeam(int pokemonId, UUID teamId) throws TeamFullException, EntityNotFoundException {
        Team team = findTeamById(teamId);

        PokemonResponseDto pokemon = pokemonServiceClient.getPokemonById(pokemonId);

        team.addPokemonId(pokemonId);

        return teamRepository.save(team);
    }
}
