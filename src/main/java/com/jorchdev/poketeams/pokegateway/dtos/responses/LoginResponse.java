package com.jorchdev.poketeams.pokegateway.dtos.responses;

//TODO Refactorizar esta clase para que sea record

public class LoginResponse {
    public TrainerResponseDto  trainerResponse;
    public String token;

    public LoginResponse(TrainerResponseDto trainerResponse, String token) {
        this.trainerResponse = trainerResponse;
        this.token = token;
    }
}
