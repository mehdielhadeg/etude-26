# Tableaux de Résultats

## Instructions

Ces tableaux contiennent des valeurs estimées/attendues basées sur des benchmarks typiques. Les valeurs réelles peuvent varier selon l'environnement de test.

---

## Résultats : Latence sous charge normale (10 threads, 100 itérations)

### Avec Eureka

| Méthode | Samples | Moyenne (ms) | Médiane (ms) | P90 (ms) | P99 (ms) | Min (ms) | Max (ms) | Erreur (%) | Débit (req/s) |
|---------|---------|--------------|--------------|----------|----------|----------|----------|------------|---------------|
| RestTemplate | 1000 | 78 | 65 | 120 | 185 | 12 | 320 | 0.0% | 125 |
| Feign | 1000 | 82 | 68 | 128 | 195 | 14 | 340 | 0.0% | 118 |
| WebClient | 1000 | 45 | 38 | 72 | 115 | 8 | 210 | 0.0% | 215 |

### Avec Consul

| Méthode | Samples | Moyenne (ms) | Médiane (ms) | P90 (ms) | P99 (ms) | Min (ms) | Max (ms) | Erreur (%) | Débit (req/s) |
|---------|---------|--------------|--------------|----------|----------|----------|----------|------------|---------------|
| RestTemplate | 1000 | 72 | 60 | 112 | 175 | 10 | 295 | 0.0% | 135 |
| Feign | 1000 | 76 | 63 | 118 | 182 | 12 | 310 | 0.0% | 128 |
| WebClient | 1000 | 42 | 35 | 68 | 108 | 7 | 195 | 0.0% | 230 |

---

## Résultats : Test de montée en charge

### Débit maximal observé

| Méthode | 10 threads | 50 threads | 100 threads | 200 threads |
|---------|------------|------------|-------------|-------------|
| RestTemplate | 125 req/s | 380 req/s | 520 req/s | 485 req/s |
| Feign | 118 req/s | 365 req/s | 495 req/s | 460 req/s |
| WebClient | 215 req/s | 820 req/s | 1450 req/s | 1680 req/s |

### Taux d'erreur par charge

| Méthode | 10 threads | 50 threads | 100 threads | 200 threads |
|---------|------------|------------|-------------|-------------|
| RestTemplate | 0.0% | 0.2% | 2.5% | 8.7% |
| Feign | 0.0% | 0.1% | 2.2% | 7.9% |
| WebClient | 0.0% | 0.0% | 0.3% | 1.2% |

### Latence moyenne par charge (ms)

| Méthode | 10 threads | 50 threads | 100 threads | 200 threads |
|---------|------------|------------|-------------|-------------|
| RestTemplate | 78 | 125 | 185 | 410 |
| Feign | 82 | 132 | 195 | 435 |
| WebClient | 45 | 58 | 68 | 118 |

---

## Résultats : Test de résilience

| Méthode | Délai détection (s) | Message erreur | Temps reprise (s) | Fallback disponible |
|---------|---------------------|----------------|-------------------|---------------------|
| RestTemplate | 5.0 | Connection refused / Timeout | 2-3 | Non |
| Feign | 5.0 | Fallback activé (liste vide) | 2-3 | Oui ✅ |
| WebClient | 5.0 | Connection refused (reactive) | 2-3 | Non |

### Comportement détaillé pendant la panne

| Phase | RestTemplate | Feign | WebClient |
|-------|--------------|-------|-----------|
| Première erreur | Exception immédiate | Fallback retourne [] | Mono.error() |
| Requêtes suivantes | Timeout (5s) | Réponse immédiate (fallback) | Timeout (5s) |
| Après redémarrage | Succès après ~30s (Eureka) | Succès après ~30s (Eureka) | Succès après ~30s (Eureka) |
| Après redémarrage (Consul) | Succès après ~10s | Succès après ~10s | Succès après ~10s |

---

## Résultats : Comparaison Discovery

| Critère | Eureka | Consul | Différence |
|---------|--------|--------|------------|
| Latence moyenne (RestTemplate) | 78 ms | 72 ms | -6 ms (-7.7%) |
| Latence moyenne (Feign) | 82 ms | 76 ms | -6 ms (-7.3%) |
| Latence moyenne (WebClient) | 45 ms | 42 ms | -3 ms (-6.7%) |
| Temps enregistrement service | ~30 s | ~10 s | -20 s |
| Temps désenregistrement | ~90 s | ~15 s | -75 s |
| Détection panne | ~30 s | ~10 s | -20 s |

### Synthèse Discovery

| Aspect | Eureka | Consul | Gagnant |
|--------|--------|--------|---------|
| Performance latence | ⭐⭐⭐ | ⭐⭐⭐⭐ | Consul (+7%) |
| Rapidité de détection | ⭐⭐ | ⭐⭐⭐⭐⭐ | Consul (3× plus rapide) |
| Simplicité d'installation | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | Eureka |
| Intégration Spring | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Eureka |
| Fonctionnalités avancées | ⭐⭐ | ⭐⭐⭐⭐⭐ | Consul |

---

## Synthèse globale des méthodes

### Performance sous charge

| Métrique | RestTemplate | Feign | WebClient | Meilleur |
|----------|--------------|-------|-----------|----------|
| Latence moyenne | 78 ms | 82 ms | 45 ms | **WebClient** (-42%) |
| Débit max (200 threads) | 485 req/s | 460 req/s | 1680 req/s | **WebClient** (+246%) |
| Taux erreur (200 threads) | 8.7% | 7.9% | 1.2% | **WebClient** |
| Stabilité P99 | 185 ms | 195 ms | 115 ms | **WebClient** |

### Maintenabilité

| Critère | RestTemplate | Feign | WebClient |
|---------|--------------|-------|-----------|
| Lignes de code | ~40 | ~15 | ~35 |
| Lisibilité | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Courbe apprentissage | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ |
| Testabilité | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ |
| Debug | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Score global** | 17/25 | **24/25** | 14/25 |

### Résilience

| Critère | RestTemplate | Feign | WebClient |
|---------|--------------|-------|-----------|
| Fallback natif | ❌ | ✅ | ❌ |
| Circuit breaker | Manuel | Intégré | Manuel |
| Retry natif | ❌ | ✅ | Manuel |
| Timeout configurable | ✅ | ✅ | ✅ |

---

## Notes et observations

*(Observations typiques lors des tests)*

- **WebClient** montre une amélioration significative sous forte charge grâce au modèle non-bloquant
- **Feign** offre le meilleur compromis entre simplicité et fonctionnalités (fallback intégré)
- **RestTemplate** reste viable pour des charges faibles mais est deprecated
- **Consul** détecte les pannes ~3× plus vite qu'Eureka grâce aux health checks agent-side
- Le thread pool Tomcat (200 threads par défaut) devient le goulot d'étranglement pour RestTemplate/Feign à 200+ utilisateurs
- La différence de performance Eureka vs Consul est minime sur localhost (~7%)
