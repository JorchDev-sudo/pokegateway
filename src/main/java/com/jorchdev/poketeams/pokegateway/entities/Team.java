package com.jorchdev.poketeams.pokegateway.entities;

import com.jorchdev.poketeams.pokegateway.exceptions.team.TeamFullException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

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
    private List<Integer> pokemonIds;

    public void addPokemonId(Integer pokemonId) throws TeamFullException {
        if (pokemonIds.size() == 6) {
            throw new TeamFullException("Pokemon teams must have 6 pokemons or less");
        }else{
            this.pokemonIds.add(pokemonId);
        }
    }
}
