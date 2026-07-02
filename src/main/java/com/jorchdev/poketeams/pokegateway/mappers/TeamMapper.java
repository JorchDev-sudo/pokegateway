package com.jorchdev.poketeams.pokegateway.mappers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TeamResponseDto;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TeamPokemonResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//TODO Modificar este mapper para que use MapStruct y así poder updatear campos

@Component
public class TeamMapper {
    private final TrainerMapper trainerMapper;

    public TeamMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    public Team toEntity (String name, Trainer trainer){
        Team team = new Team();

        team.setName(name);
        team.setTrainer(trainer);

        return team;
    }

    public TeamResponseDto toDto (Team team){
        return new TeamResponseDto(
                team.getId(),
                team.getName(),
                trainerMapper.toDto(team.getTrainer()),
                map(team.getPokemons()));

    }

    static List<TeamPokemonResponseDto> map(Map<Integer, String> pokemons) {
        return pokemons.entrySet()
                .stream()
                .map(entry ->
                        new TeamPokemonResponseDto(
                                entry.getKey(),
                                entry.getValue()))
                .toList();
    }
}
