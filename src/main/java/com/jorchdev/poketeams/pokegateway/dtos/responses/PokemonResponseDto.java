package com.jorchdev.poketeams.pokegateway.dtos.responses;

import java.util.List;

//TODO Refactorizar esta clase para que sea record

public class PokemonResponseDto {
    public int id;
    public String name;
    public List<String> types;
    public List<String> moves;
}
