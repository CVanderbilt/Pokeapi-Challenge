package com.alea.challenge.model;

import java.util.Comparator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Pokemon {
    @Getter
    String url;
    
    @Getter
    String name;

    @Getter
    int base_experience;

    @Getter
    int height;

    @Getter
    int weight;

    public String buildStatsString() {
        return String.format("Name: %s\nBase Experience: %d\nHeight: %d\nWeight: %d",
                             name, base_experience, height, weight);
    }

    public static Comparator<Pokemon> heightComparator = Comparator.comparingInt(Pokemon::getHeight).reversed();
    public static Comparator<Pokemon> weightComparator = Comparator.comparingInt(Pokemon::getWeight).reversed();
    public static Comparator<Pokemon> experienceComparator = Comparator.comparingInt(Pokemon::getBase_experience).reversed();
}
