package com.jorchdev.poketeams.pokegateway.entities.internal.inputs;

public record ChangePasswordInput(
        String currentPassword,
        String newPassword
) {}
