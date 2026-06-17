package com.jorchdev.poketeams.pokegateway.entities.internal;

public record ChangePasswordInput(
        String currentPassword,
        String newPassword
) {}
