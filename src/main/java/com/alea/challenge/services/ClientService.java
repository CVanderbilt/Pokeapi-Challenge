package com.alea.challenge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    public ClientService() {}

    public String getHeaviestPokemons() {
        return "heaviest";
    }

    public String getHighestPokemons() {
        return "highest";
    }

    public String getMoreExperiencedPokemons() {
        return "moreExperienced";
    }
}