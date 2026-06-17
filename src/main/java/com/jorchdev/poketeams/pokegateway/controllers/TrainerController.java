package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.ChangePasswordInput;
import com.jorchdev.poketeams.pokegateway.entities.internal.UpdateProfileInput;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerNotFoundException;
import com.jorchdev.poketeams.pokegateway.mappers.TrainerMapper;
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
    private final TrainerMapper trainerMapper;

    public TrainerController(
            TrainerService trainerService,
            TrainerMapper trainerMapper)
    {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    @QueryMapping
    public TrainerResponseDto me(Authentication auth){
        System.out.println("Hola! " + auth);

        Trainer trainer = trainerService.findTrainerByEmail(auth.getName())
                .orElseThrow(() -> new TrainerNotFoundException("Something went wrong"));

        return trainerMapper.toDto(trainer);
    }

    @QueryMapping
    public TrainerResponseDto findTrainerById(@Argument UUID id){
        Trainer trainer = trainerService.findTrainerById(id)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with id " + id + " not found"));

        return trainerMapper.toDto(trainer);
    }

    @QueryMapping
    public TrainerResponseDto findTrainerByName(@Argument String name){
        Trainer trainer = trainerService.findTrainerByName(name)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with name " + name + " not found"));

        return trainerMapper.toDto(trainer);
    }

    @MutationMapping
    public TrainerResponseDto updateProfile(
            @Argument UpdateProfileInput input,
            Authentication authentication) {

        return trainerService.updateTrainer(
                input,
                authentication.getName());
    }

    @MutationMapping
    public Boolean changePassword(
            @Argument ChangePasswordInput input,
            Authentication authentication) {

        trainerService.changePassword(
                authentication.getName(),
                input);

        return true;
    }

    @MutationMapping
    public Boolean deleteTrainer(Authentication authentication) {
        return trainerService.deleteTrainer(authentication.getName());
    }
}
