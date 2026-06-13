package com.jorchdev.poketeams.pokegateway.dtos.requests;

public class TrainerRequestDto {
    @NotBlank
    public String name;
    public String email;
    public String password;

}
