package com.jorchdev.poketeams.pokegateway.entities;

import com.jorchdev.poketeams.pokegateway.exceptions.team.TeamFullException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
public class Team {
    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private String name;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ElementCollection(fetch = FetchType.EAGER)
    private final Map<Integer, String> pokemons =  new HashMap<>();

    public void addPokemon(Integer pokemonId, String pokemonName) throws TeamFullException {
        if (pokemons.size() >= 6) {
            throw new TeamFullException("Pokemon teams must have 6 pokemons or less");
        }else{
            this.pokemons.put(pokemonId, pokemonName);
        }
    }

    public void removePokemon(int pokemonId){
        pokemons.remove(pokemonId);
    }
    public void removePokemonByName(String name){
        pokemons.values().removeIf(x -> x.equals(name));
    }
}
