package com.jorchdev.poketeams.pokegateway.repositories;

import com.jorchdev.poketeams.pokegateway.entities.internal.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<UserSession,String> {
}
