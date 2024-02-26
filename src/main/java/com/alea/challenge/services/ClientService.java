package com.alea.challenge.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.alea.challenge.model.Pokemon;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import reactor.core.publisher.Mono;

@Service
public class ClientService {
    private static final String CATEGORY_WEIGHT = "weight";
    private static final String CATEGORY_HEIGHT = "height";
    private static final String CATEGORY_BASE_EXP = "base_experience";
    private static String MESSAGE_TEMPLATE = "The top pokemons with highest value in %s are:\n%s";
    
    private final WebClient webClient;
    private final Gson gson;
    private final CacheManager cacheManager;

    @Autowired
    public ClientService(WebClient webClient, Gson gson, CacheManager cacheManager) {
        this.webClient = webClient;
        this.gson = gson;
        this.cacheManager = cacheManager;
    }

    public String getHeaviestPokemons() {
        try {
            return BuildResponseString(CATEGORY_WEIGHT, getFilteredPokemon(CATEGORY_WEIGHT));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    public String getHighestPokemons() {
        try {
            return BuildResponseString(CATEGORY_HEIGHT, getFilteredPokemon(CATEGORY_HEIGHT));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";

        }
    }

    public String getMoreExperiencedPokemons() {
        try {
            return BuildResponseString(CATEGORY_BASE_EXP, getFilteredPokemon(CATEGORY_BASE_EXP));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }
    }

    private String BuildResponseString(String category, TreeSet<Pokemon> pokemons) {
        String pokemonDetails = pokemons.stream()
            .map(pokemon -> {
                String additionalInfo;
                switch (category) {
                    case "base_experience":
                        additionalInfo = ": " + pokemon.getBase_experience();
                        break;
                    case "height":
                        additionalInfo = ": " + pokemon.getHeight();
                        break;
                    case "weight":
                        additionalInfo = ": " + pokemon.getWeight();
                        break;
                    default:
                        additionalInfo = "";
                }
                return pokemon.getName() + additionalInfo;
            })
            .collect(Collectors.joining("\n"));

        return String.format(MESSAGE_TEMPLATE, category, pokemonDetails);
    }

    private TreeSet<Pokemon> getFilteredPokemon(String category) throws Exception {
        Cache cache = cacheManager.getCache("filteredPokemon");
        Cache.ValueWrapper valueWrapper = cache.get(category);

        if (valueWrapper != null) {
            return (TreeSet<Pokemon>) valueWrapper.get();
        } else {
            List<Pokemon> pokemons = getAllPokemons();
            TreeSet<Pokemon> topWeight = new TreeSet<>(Pokemon.weightComparator);
            TreeSet<Pokemon> topHeight = new TreeSet<>(Pokemon.heightComparator);
            TreeSet<Pokemon> topExperience = new TreeSet<>(Pokemon.experienceComparator);

            for (Pokemon pokemon : pokemons) {
                updateTopSet(topHeight, pokemon, Pokemon.heightComparator);
                updateTopSet(topWeight, pokemon, Pokemon.weightComparator);
                updateTopSet(topExperience, pokemon, Pokemon.experienceComparator);
            }

            cache.put(CATEGORY_BASE_EXP, topExperience);
            cache.put(CATEGORY_HEIGHT, topHeight);
            cache.put(CATEGORY_WEIGHT, topWeight);

            switch (category) {
                case CATEGORY_BASE_EXP: return topExperience;
                case CATEGORY_HEIGHT: return topHeight;
                case CATEGORY_WEIGHT: return topWeight;
                default: throw new Exception("Invalid category");
            }
        }
    }

    private static void updateTopSet(TreeSet<Pokemon> mySet, Pokemon toAdd, Comparator<Pokemon> comparator) {
        if (mySet.size() < 5) {
            mySet.add(toAdd);
        } else {
            Pokemon smallest = mySet.last();
            if (comparator.compare(toAdd, smallest) < 0) {
                mySet.pollLast(); // Remove the smallest element
                mySet.add(toAdd);
            }
        }
    }

    private List<Pokemon> getAllPokemons() {
        List<String> names = getAllPokemonURLs();
        List<Pokemon> pokemons = new ArrayList<>();
        
        System.out.println("Fetching all pokemons, this may take a while");
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            double progress = ((double) i / names.size() * 100);
            String formattedProgress = String.format("%.2f", progress);
            System.out.print("\rProgress: " + formattedProgress + "%");
            
            RequestHeadersUriSpec a = webClient.get();
            RequestHeadersSpec b = a.uri(name);
            ResponseSpec c = b.retrieve();
            Mono<byte[]> d = c.bodyToMono(byte[].class);
            Mono<byte[]> responseBody = webClient
                .get()
                .uri(name)
                .retrieve()
                .bodyToMono(byte[].class);

            String jsonResponse = responseBody.map(String::new).block();
    
            try {
                Pokemon pokemon = gson.fromJson(jsonResponse, Pokemon.class);
                pokemons.add(pokemon);
                //String jsonOutput = gson.toJson(pokemon);
                //System.out.println("\njson -> ======\n" + jsonResponse + "\n=========\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("\rProgress: 100%");
    
        return pokemons;
    }

    private List<String> getAllPokemonURLs() {
        String jsonResponse = webClient
                .get().uri("/pokemon/?limit=-1")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<String> pokemonNames = new ArrayList<>();

        try {
            JsonElement jsonElement = gson.fromJson(jsonResponse, JsonElement.class);

            if (jsonElement != null && jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("results") && jsonObject.get("results").isJsonArray()) {
                    JsonArray resultsArray = jsonObject.getAsJsonArray("results");
                    for (JsonElement element : resultsArray) {
                        if (element.isJsonObject() && element.getAsJsonObject().has("url")) {
                            pokemonNames.add(element.getAsJsonObject().get("url").getAsString());
                        }
                    }
                } else {
                    System.err.println("Unexpected JSON structure");
                }
            } else {
                System.err.println("Unexpected JSON structure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pokemonNames;
    }
}