package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponseDto;
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
    private final PasswordEncoder passwordEncoder;

    public TrainerService(
            TrainerRepository trainerRepository,
            TrainerMapper trainerMapper,
            TrainerHelper trainerHelper,
            PasswordEncoder passwordEncoder)
    {
        this.repository = trainerRepository;
        this.mapper = trainerMapper;
        this.passwordEncoder = passwordEncoder;
        this.helper = trainerHelper;
    }

    public TrainerResponseDto createTrainer(Trainer request) {
        Trainer trainer = helper.createTrainer(request);
        Trainer savedTrainer = repository.save(trainer);

        return mapper.toDto(savedTrainer);
    }

    public TrainerResponseDto findTrainerById(UUID id){
        Trainer trainer = helper.getTrainerById(id);

        return mapper.toDto(trainer);
    }

    public TrainerResponseDto findTrainerByName(String name){
        Trainer trainer = helper.getTrainerByName(name);

        return mapper.toDto(trainer);
    }

    public TrainerResponseDto updateTrainer(UpdateProfileInput input, String email){
        Trainer trainer = helper.getTrainerByEmail(email);

        trainer.setName(input.name());
        trainer.setEmail(input.email());

        Trainer savedTrainer = repository.save(trainer);

        return mapper.toDto(savedTrainer);
    }

    public void changePassword(
            String email,
            ChangePasswordInput input) {

        Trainer trainer = helper.getTrainerByEmail(email);

        if (!passwordEncoder.matches(
                input.currentPassword(),
                trainer.getPassword())) {

            throw new BadCredentialsException(
                    "Current password is incorrect");
        }

        trainer.setPassword(
                passwordEncoder.encode(
                        input.newPassword()));

        repository.save(trainer);
    }

    public void deleteTrainer(String email) {
        Trainer trainer = helper.getTrainerByEmail(email);

        repository.delete(trainer);
    }
}
