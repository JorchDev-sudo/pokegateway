package com.jorchdev.poketeams.pokegateway.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class Pokemon {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    private int pokemonId;

    private String name;

    @Setter
    private String nickname;

    private List<String> types;

    private List<String> moves;

    @Setter
    protected int position;

    public Pokemon(){}

    public Pokemon(String nickname) {
        this.nickname = nickname;
    }

    public Pokemon(Team team, int pokemonId, String name, int position) {
        this.team = team;
        this.pokemonId = pokemonId;
        this.name = name;
        this.position = position;
    }
}