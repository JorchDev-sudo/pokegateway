package com.jorchdev.poketeams.pokegateway.entities.internal;

import com.jorchdev.poketeams.pokegateway.entities.Trainer;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomUserDetails
        implements UserDetails {

    @Getter
    private final UUID id;
    private final String email;
    private final String password;

    public CustomUserDetails(
            Trainer trainer) {

        this.id = trainer.getId();
        this.email = trainer.getEmail();
        this.password = trainer.getPassword();
    }

    @NonNull
    @Override
    public String getUsername() {
        return email;
    }

    @NonNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }
}