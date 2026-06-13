package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public Trainer registerTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public Optional<Trainer> findTrainerById(UUID id){
        return trainerRepository.findById(id);
    }

    public Optional<Trainer> findTrainerByName(String name){
        return trainerRepository.findByName(name);
    }
}
