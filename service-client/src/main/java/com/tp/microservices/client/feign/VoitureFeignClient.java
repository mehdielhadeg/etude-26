package com.tp.microservices.client.feign;

import com.tp.microservices.client.model.VoitureDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Client Feign pour communiquer avec service-voiture.
 * 
 * Avantages:
 * - Déclaratif (annotation-driven)
 * - Intégration native avec Eureka/Consul
 * - Facile à tester (mock)
 * - Support du circuit breaker
 * - Code minimal, très lisible
 * 
 * Inconvénients:
 * - Bloquant par défaut
 * - Overhead de génération de proxy
 * - Moins flexible pour des cas complexes
 */
@FeignClient(name = "service-voiture", fallback = VoitureFeignClientFallback.class)
public interface VoitureFeignClient {

    /**
     * Récupère toutes les voitures
     */
    @GetMapping("/api/voitures")
    List<VoitureDTO> getAllVoitures();

    /**
     * Récupère une voiture par ID
     */
    @GetMapping("/api/voitures/{id}")
    VoitureDTO getVoitureById(@PathVariable("id") Long id);

    /**
     * Récupère les voitures d'un client
     */
    @GetMapping("/api/voitures/client/{clientId}")
    List<VoitureDTO> getVoituresByClientId(@PathVariable("clientId") Long clientId);
}
