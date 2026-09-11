package com.jorchdev.poketeams.pokegateway.controllers;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponse;
import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.ChangePasswordInput;
import com.jorchdev.poketeams.pokegateway.entities.internal.CustomUserDetails;
import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.UpdateProfileInput;
import com.jorchdev.poketeams.pokegateway.exceptions.AuthException;
import com.jorchdev.poketeams.pokegateway.services.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import javax.naming.AuthenticationException;
import java.util.UUID;

@Slf4j
@Controller
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService)
    {
        this.trainerService = trainerService;
    }

    @QueryMapping
    public TrainerResponse me(Authentication auth){
        Object principal = auth.getPrincipal();

        try {
            CustomUserDetails user = (CustomUserDetails) principal;
            assert user != null;

            UUID trainerId = user.getId();

            return trainerService.findTrainerById(trainerId);
        } catch (Exception ex){
            throw new AuthException("Not Authenticated");
        }
    }

    @QueryMapping
    public TrainerResponse findTrainerById(@Argument UUID id){
        return trainerService.findTrainerById(id);
    }

    @QueryMapping
    public TrainerResponse findTrainerByName(@Argument String name){
        return trainerService.findTrainerByName(name);
    }

    @MutationMapping
    public TrainerResponse updateProfile(@Argument UpdateProfileInput input, Authentication authentication) {
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
