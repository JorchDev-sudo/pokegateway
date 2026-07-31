package com.jorchdev.poketeams.pokegateway.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class TeamPokemon {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    private int pokemonId;

    @Setter
    private String pokemonName;

    private String name;

    private List<String> types;

    private List<String> moves;

    @Setter
    protected int position;

    public TeamPokemon(){}

    public TeamPokemon(String name) {
        this.name = name;
    }

    public TeamPokemon(Team team, int pokemonId, String pokemonName, int position) {
        this.team = team;
        this.pokemonId = pokemonId;
        this.pokemonName = pokemonName;
        this.position = position;
    }
}