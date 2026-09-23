package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponse;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.ChangePasswordInput;
import com.jorchdev.poketeams.pokegateway.entities.internal.inputs.UpdateProfileInput;
import com.jorchdev.poketeams.pokegateway.mappers.TrainerMapper;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import com.jorchdev.poketeams.pokegateway.services.helpers.TrainerHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainerService {
    private final TrainerRepository repository;
    private final TrainerMapper mapper;
    private final TrainerHelper helper;

    public TrainerService(
            TrainerRepository trainerRepository,
            TrainerMapper trainerMapper,
            TrainerHelper trainerHelper,
            PasswordEncoder passwordEncoder)
    {
        this.repository = trainerRepository;
        this.mapper = trainerMapper;
        this.helper = trainerHelper;
    }

    public TrainerResponse findTrainerById(UUID id){
        Trainer trainer = helper.getTrainerById(id);

        return mapper.toDto(trainer);
    }

    public TrainerResponse findTrainerByName(String name){
        Trainer trainer = helper.getTrainerByName(name);

        return mapper.toDto(trainer);
    }

    public TrainerResponse updateTrainer(UpdateProfileInput input, String email){
        Trainer trainer = helper.getTrainerByEmail(email);

        trainer.setName(input.name());
        trainer.setEmail(input.email());

        Trainer savedTrainer = repository.save(trainer);

        return mapper.toDto(savedTrainer);
    }
}
