package com.jorchdev.poketeams.pokegateway.services;

import com.jorchdev.poketeams.pokegateway.dtos.responses.TrainerResponseDto;
import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.ChangePasswordInput;
import com.jorchdev.poketeams.pokegateway.entities.internal.UpdateProfileInput;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerNotFoundException;
import com.jorchdev.poketeams.pokegateway.mappers.TrainerMapper;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrainerMapper trainerMapper;

    public TrainerService(
            TrainerRepository trainerRepository,
            TrainerMapper trainerMapper,
            PasswordEncoder passwordEncoder)
    {
        this.trainerRepository = trainerRepository;
        this.trainerMapper = trainerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Trainer createTrainer(Trainer trainer) { return trainerRepository.save(trainer); }

    public Optional<Trainer> findTrainerById(UUID id){ return trainerRepository.findById(id); }
    public Optional<Trainer> findTrainerByName(String name){ return trainerRepository.findByName(name); }
    public Optional<Trainer> findTrainerByEmail(String email){ return trainerRepository.findByEmail(email); }

    public TrainerResponseDto updateTrainer(UpdateProfileInput input, String email){
        Trainer trainer = trainerRepository
                .findByEmail(email)
                .orElseThrow(() -> new TrainerNotFoundException("Something went wrong"));

        trainer.setName(input.name());
        trainer.setEmail(input.email());

        Trainer savedTrainer = trainerRepository.save(trainer);

        return trainerMapper.toDto(savedTrainer);
    }

    public void changePassword(
            String email,
            ChangePasswordInput input) {

        Trainer trainer = trainerRepository
                .findByEmail(email)
                .orElseThrow(() -> new TrainerNotFoundException("Something went wrong"));

        if (!passwordEncoder.matches(
                input.currentPassword(),
                trainer.getPassword())) {

            throw new BadCredentialsException(
                    "Current password is incorrect");
        }

        trainer.setPassword(
                passwordEncoder.encode(
                        input.newPassword()));

        trainerRepository.save(trainer);
    }

    public Boolean deleteTrainer(String email) {
        Trainer trainer = trainerRepository
                .findByEmail(email)
                .orElseThrow(() -> new TrainerNotFoundException("Something went wrong"));

        trainerRepository.delete(trainer);

        return true;
    }
}
