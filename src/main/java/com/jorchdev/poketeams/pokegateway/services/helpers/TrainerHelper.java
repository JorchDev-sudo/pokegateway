package com.jorchdev.poketeams.pokegateway.services.helpers;

import com.jorchdev.poketeams.pokegateway.entities.Team;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.CustomUserDetails;
import com.jorchdev.poketeams.pokegateway.exceptions.BadCredentialsException;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerNotFoundException;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrainerHelper {
    private final TrainerRepository repository;

    public TrainerHelper(TrainerRepository repository) {
        this.repository = repository;
    }

    public Trainer getCurrentTrainer(Authentication auth){
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        assert user != null;

        UUID trainerId = user.getId();

        return getTrainerById(trainerId);
    }

    public Trainer getTrainerById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with id " + id + " not found"));
    }

    public Trainer getTrainerByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with name " + name + " not found"));
    }

    public Trainer getTrainerByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with email " + email + " not found"));
    }

    public Trainer createTrainer(Trainer trainer) {
        if (repository.findByEmail(trainer.getEmail()).isPresent()) {
            throw new BadCredentialsException("Trainer with email " + trainer.getEmail() + " already exists");

        }else if (repository.findByName(trainer.getName()).isPresent()) {
            throw new BadCredentialsException("Trainer with name " + trainer.getName() + " already exists");
        }

        return trainer;
    }

    public void asignTeamToTrainer(Trainer trainer, Team team){
        trainer.setTeam(team);
        repository.save(trainer);
    }
}
