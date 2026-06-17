package com.jorchdev.poketeams.pokegateway.services.security;

import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.entities.internal.CustomUserDetails;
import com.jorchdev.poketeams.pokegateway.exceptions.trainer.TrainerNotFoundException;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final TrainerRepository trainerRepository;

    public CustomUserDetailsService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @NonNull
    @Override
    public CustomUserDetails loadUserByUsername(@NonNull String email) {
        Trainer trainer = trainerRepository.findByEmail(email)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with email " + email + " not found"));

        return new CustomUserDetails(trainer);
    }

    public CustomUserDetails loadUserById(@NonNull UUID id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer with id " + id + " not found"));

        return new CustomUserDetails(trainer);
    }
}
