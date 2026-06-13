package com.jorchdev.poketeams.pokegateway.services.auth;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponseDto;
import com.jorchdev.poketeams.pokegateway.mappers.TrainerMapper;
import com.jorchdev.poketeams.pokegateway.services.TrainerService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    public AuthService(
            TrainerService trainerService,
            TrainerMapper trainerMapper)
    {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    public TrainerResponseDto register ()
}
