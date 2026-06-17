package com.jorchdev.poketeams.pokegateway.dtos.responses;

public class LoginResponse {
    public TrainerResponseDto  trainerResponse;
    public String token;

    public LoginResponse(TrainerResponseDto trainerResponse, String token) {
        this.trainerResponse = trainerResponse;
        this.token = token;
    }
}
