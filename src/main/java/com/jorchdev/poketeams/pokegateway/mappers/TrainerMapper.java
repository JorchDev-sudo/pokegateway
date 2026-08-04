package com.jorchdev.poketeams.pokegateway.mappers;

import com.jorchdev.poketeams.pokegateway.dtos.requests.RegisterRequestDto;
import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TrainerSummary;
import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponse;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {
    public TrainerResponse toDto(Trainer trainer) {

        return new TrainerResponse(
                trainer.getId(),
                trainer.getName(),
                trainer.getEmail(),
                TeamMapper.toSummary(
                        trainer.getTeam()
                )
        );
    }

    public static TrainerSummary toBasicDto(Trainer trainer) {
        return new TrainerSummary(
                trainer.getId(),
                trainer.getName(),
                trainer.getEmail()
        );
    }

    public Trainer toEntity(RegisterRequestDto request) {
        Trainer entity = new Trainer();

        entity.setEmail(request.email);
        entity.setName(request.name);

        return entity;
    }

}
