package com.jorchdev.poketeams.pokegateway.services.helpers;

import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.DuplicateTrainerException;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerNotFoundException;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrainerHelper {
    private final TrainerRepository repository;

    public TrainerHelper(TrainerRepository repository) {
        this.repository = repository;
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
            throw new DuplicateTrainerException("Trainer with email " + trainer.getEmail() + " already exists");

        }else if (repository.findByName(trainer.getName()).isPresent()) {
            throw new DuplicateTrainerException("Trainer with name " + trainer.getName() + " already exists");
        }

        return trainer;
    }
}
