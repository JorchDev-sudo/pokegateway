package com.jorchdev.poketeams.pokegateway.services.helpers;

import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.repositories.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TeamHelper {
    private final TeamRepository teamRepository;

    public TeamHelper(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getTeamById(UUID teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
    }
}
