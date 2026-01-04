package com.tp.microservices.client.feign;

import com.tp.microservices.client.model.VoitureDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback pour le client Feign en cas d'échec du service voiture.
 * Permet de fournir une réponse par défaut en cas de panne.
 */
@Component
public class VoitureFeignClientFallback implements VoitureFeignClient {

    @Override
    public List<VoitureDTO> getAllVoitures() {
        System.err.println("Fallback: service-voiture indisponible - getAllVoitures");
        return Collections.emptyList();
    }

    @Override
    public VoitureDTO getVoitureById(Long id) {
        System.err.println("Fallback: service-voiture indisponible - getVoitureById(" + id + ")");
        return null;
    }

    @Override
    public List<VoitureDTO> getVoituresByClientId(Long clientId) {
        System.err.println("Fallback: service-voiture indisponible - getVoituresByClientId(" + clientId + ")");
        return Collections.emptyList();
    }
}
