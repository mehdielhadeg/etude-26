package com.tp.microservices.client.config;

import com.tp.microservices.client.model.Client;
import com.tp.microservices.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;

    @Override
    public void run(String... args) {
        // Données de test
        clientRepository.save(new Client(null, "Dupont", "Jean", "jean.dupont@email.com", "0612345678", null));
        clientRepository.save(new Client(null, "Martin", "Marie", "marie.martin@email.com", "0623456789", null));
        clientRepository.save(new Client(null, "Bernard", "Pierre", "pierre.bernard@email.com", "0634567890", null));
    }
}
