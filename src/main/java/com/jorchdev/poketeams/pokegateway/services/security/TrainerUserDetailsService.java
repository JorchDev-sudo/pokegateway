package com.jorchdev.poketeams.pokegateway.services.security;

import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import com.jorchdev.poketeams.pokegateway.repositories.TrainerRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerUserDetailsService implements UserDetailsService {
    private final TrainerRepository trainerRepository;

    public TrainerUserDetailsService(
            TrainerRepository trainerRepository) {

        this.trainerRepository = trainerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email)
            throws UsernameNotFoundException {

        Trainer trainer = trainerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Trainer not found"));

        return User.builder()
                .username(trainer.getEmail())
                .password(trainer.getPassword())
                .authorities(List.of())
                .build();
    }
}