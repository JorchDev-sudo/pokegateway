package com.jorchdev.poketeams.pokegateway.mappers;

import com.jorchdev.poketeams.pokegateway.dtos.requests.RegisterRequestDto;
import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {
    private final TeamMapper teamMapper;

    public TrainerMapper(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    public TrainerResponseDto toDto(Trainer trainer) {
        TrainerResponseDto response = new TrainerResponseDto();

        response.id = trainer.getId();
        response.name = trainer.getName();
        response.email = trainer.getEmail();
        response.team = teamMapper.toSummary(trainer.getTeam());

        return response;
    }

    public Trainer toEntity(RegisterRequestDto request) {
        Trainer entity = new Trainer();

        entity.setEmail(request.email);
        entity.setName(request.name);

        return entity;
    }

}
