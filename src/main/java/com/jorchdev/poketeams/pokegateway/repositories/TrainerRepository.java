package com.jorchdev.poketeams.pokegateway.repositories;

import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID> {
    Optional<Trainer> findByName(String name);
}
