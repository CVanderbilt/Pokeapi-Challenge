package com.alea.challenge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alea.challenge.services.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/heaviest")
    public String getHeaviestPokemons() {
        return clientService.getHeaviestPokemons();
    }

    @GetMapping("/highest")
    public String getHighestPokemons() {
        return clientService.getHighestPokemons();
    }

    @GetMapping("/moreExperienced")
    public String getMoreExperiencedPokemons() {
        return clientService.getMoreExperiencedPokemons();
    }
}