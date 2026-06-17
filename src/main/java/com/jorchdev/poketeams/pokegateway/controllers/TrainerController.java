package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.internal.ChangePasswordInput;
import com.jorchdev.poketeams.pokegateway.entities.internal.CustomUserDetails;
import com.jorchdev.poketeams.pokegateway.entities.internal.UpdateProfileInput;
import com.jorchdev.poketeams.pokegateway.services.TrainerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService)
    {
        this.trainerService = trainerService;
    }

    @QueryMapping
    public TrainerResponseDto me(Authentication auth){
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        assert user != null;
        UUID trainerId = user.getId();

        return trainerService.findTrainerById(trainerId);
    }

    @QueryMapping
    public TrainerResponseDto findTrainerById(@Argument UUID id){
        return trainerService.findTrainerById(id);
    }

    @QueryMapping
    public TrainerResponseDto findTrainerByName(@Argument String name){
        return trainerService.findTrainerByName(name);
    }

    @MutationMapping
    public TrainerResponseDto updateProfile(@Argument UpdateProfileInput input, Authentication authentication) {
        return trainerService.updateTrainer(
                input,
                authentication.getName());
    }

    @MutationMapping
    public void changePassword(@Argument ChangePasswordInput input, Authentication authentication) {
        trainerService.changePassword(authentication.getName(), input);
    }

    @MutationMapping
    public void deleteTrainer(Authentication authentication) {
        trainerService.deleteTrainer(authentication.getName());
    }
}
