package com.jorchdev.poketeams.pokegateway.repositories;

import com.jorchdev.poketeams.pokegateway.entities.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, UUID> {
    Optional<Pokemon> findByNickname(String nickname);
}
