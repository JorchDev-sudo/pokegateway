package com.jorchdev.poketeams.pokegateway.services.helpers;

import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.exceptions.BadCredentialsException;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerException;
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

    public Trainer getCurrentTrainer(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof Trainer trainer)) {
            throw new IllegalStateException("No authenticated trainer found in security context");
        }
        return trainer;
    }

    public Trainer getTrainerById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TrainerException("Trainer with id " + id + " not found"));
    }

    public Trainer getTrainerByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new TrainerException("Trainer with name " + name + " not found"));
    }

    public Trainer getTrainerByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new TrainerException("Trainer with email " + email + " not found"));
    }
}
