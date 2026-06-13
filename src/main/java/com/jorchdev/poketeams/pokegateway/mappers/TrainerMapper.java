package com.jorchdev.poketeams.pokegateway.mappers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;

public class TrainerMapper {
    public TrainerResponseDto toDto(Trainer trainer) {
        TrainerResponseDto trainerResponseDto = new TrainerResponseDto();

        trainerResponseDto.id = trainer.getId();
        trainerResponseDto.name = trainer.getName();

        return trainerResponseDto;
    }
}
