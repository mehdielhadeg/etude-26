package com.tp.microservices.client.webclient;

import com.tp.microservices.client.model.VoitureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * Service utilisant WebClient pour communiquer avec service-voiture.
 * 
 * Avantages:
 * - Non-bloquant et réactif
 * - Meilleure utilisation des ressources
 * - Support du streaming
 * - API fluide et moderne
 * - Excellente performance sous charge
 * 
 * Inconvénients:
 * - Courbe d'apprentissage (paradigme réactif)
 * - Debugging plus complexe
 * - Stack trace moins lisible
 */
@Service
@RequiredArgsConstructor
public class VoitureWebClientService {

    private final WebClient webClient;

    /**
     * Récupère toutes les voitures via WebClient (bloquant pour comparaison)
     */
    public List<VoitureDTO> getAllVoitures() {
        try {
            return webClient.get()
                    .uri("/api/voitures")
                    .retrieve()
                    .bodyToFlux(VoitureDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("WebClient Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Récupère toutes les voitures de manière réactive
     */
    public Flux<VoitureDTO> getAllVoituresReactive() {
        return webClient.get()
                .uri("/api/voitures")
                .retrieve()
                .bodyToFlux(VoitureDTO.class)
                .onErrorResume(e -> {
                    System.err.println("WebClient Reactive Error: " + e.getMessage());
                    return Flux.empty();
                });
    }

    /**
     * Récupère une voiture par ID via WebClient
     */
    public VoitureDTO getVoitureById(Long id) {
        try {
            return webClient.get()
                    .uri("/api/voitures/{id}", id)
                    .retrieve()
                    .bodyToMono(VoitureDTO.class)
                    .block();
        } catch (Exception e) {
            System.err.println("WebClient Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Récupère une voiture par ID de manière réactive
     */
    public Mono<VoitureDTO> getVoitureByIdReactive(Long id) {
        return webClient.get()
                .uri("/api/voitures/{id}", id)
                .retrieve()
                .bodyToMono(VoitureDTO.class)
                .onErrorResume(e -> {
                    System.err.println("WebClient Reactive Error: " + e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Récupère les voitures d'un client via WebClient
     */
    public List<VoitureDTO> getVoituresByClientId(Long clientId) {
        try {
            return webClient.get()
                    .uri("/api/voitures/client/{clientId}", clientId)
                    .retrieve()
                    .bodyToFlux(VoitureDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("WebClient Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les voitures d'un client de manière réactive
     */
    public Flux<VoitureDTO> getVoituresByClientIdReactive(Long clientId) {
        return webClient.get()
                .uri("/api/voitures/client/{clientId}", clientId)
                .retrieve()
                .bodyToFlux(VoitureDTO.class)
                .onErrorResume(e -> {
                    System.err.println("WebClient Reactive Error: " + e.getMessage());
                    return Flux.empty();
                });
    }
}
