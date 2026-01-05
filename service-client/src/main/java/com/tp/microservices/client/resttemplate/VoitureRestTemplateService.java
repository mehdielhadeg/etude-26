package com.tp.microservices.client.resttemplate;

import com.tp.microservices.client.model.VoitureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Service utilisant RestTemplate pour communiquer avec service-voiture.
 * 
 * Avantages:
 * - API simple et synchrone
 * - Bien documenté, mature
 * - Facile à débugger
 * 
 * Inconvénients:
 * - Bloquant (thread par requête)
 * - Deprecated depuis Spring 5 (remplacé par WebClient)
 * - Mauvaise performance sous charge élevée
 */
@Service
@RequiredArgsConstructor
public class VoitureRestTemplateService {

    private final RestTemplate restTemplate;

    /**
     * Récupère toutes les voitures via RestTemplate
     */
    public List<VoitureDTO> getAllVoitures() {
        try {
            ResponseEntity<List<VoitureDTO>> response = restTemplate.exchange(
                    "http://service-voiture/api/voitures",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<VoitureDTO>>() {
                    });
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("RestTemplate Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Récupère une voiture par ID via RestTemplate
     */
    public VoitureDTO getVoitureById(Long id) {
        try {
            return restTemplate.getForObject(
                    "http://service-voiture/api/voitures/{id}",
                    VoitureDTO.class,
                    id);
        } catch (Exception e) {
            System.err.println("RestTemplate Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Récupère les voitures d'un client via RestTemplate
     */
    public List<VoitureDTO> getVoituresByClientId(Long clientId) {
        try {
            ResponseEntity<List<VoitureDTO>> response = restTemplate.exchange(
                    "http://service-voiture/api/voitures/client/{clientId}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<VoitureDTO>>() {
                    },
                    clientId);
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("RestTemplate Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
