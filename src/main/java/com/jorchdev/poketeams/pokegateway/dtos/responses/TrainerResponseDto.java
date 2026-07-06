package com.jorchdev.poketeams.pokegateway.dtos.responses;

import com.jorchdev.poketeams.pokegateway.dtos.responses.internal.TeamSummary;

import java.util.UUID;

//TODO Refactorizar esta clase para que sea record

public class TrainerResponseDto {
    public UUID id;
    public String name;
    public String email;
    public TeamSummary team;
}
