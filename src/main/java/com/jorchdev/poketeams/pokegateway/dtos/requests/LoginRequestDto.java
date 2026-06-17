package com.jorchdev.poketeams.pokegateway.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDto {
    @Email
    @Size(min = 3, max = 40)
    public String email;

    @NotBlank
    @Size(min = 6, max = 30)
    public String password;
}
