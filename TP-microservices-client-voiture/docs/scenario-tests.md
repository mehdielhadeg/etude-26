# Scénarios de Tests

## Objectif des tests

Comparer les performances et la résilience de trois méthodes de communication inter-services :
1. **RestTemplate** (synchrone, traditionnel)
2. **Feign Client** (déclaratif, avec load balancing)
3. **WebClient** (réactif, non-bloquant)

---

## Scénario 1 : Test de latence sous charge normale

### Description
Mesurer le temps de réponse moyen pour chaque méthode avec une charge modérée.

### Configuration
| Paramètre | Valeur |
|-----------|--------|
| Utilisateurs concurrent | 10 |
| Itérations | 100 |
| Durée de montée en charge | 5 secondes |

### Métriques collectées
- Temps de réponse moyen (ms)
- Temps de réponse médian (ms)
- Percentile 90 (ms)
- Percentile 99 (ms)
- Taux d'erreur (%)

---

## Scénario 2 : Test de débit maximal

### Description
Déterminer le nombre maximal de requêtes par seconde que chaque méthode peut gérer.

### Configuration
| Paramètre | Valeur |
|-----------|--------|
| Utilisateurs concurrent | 50, 100, 200 |
| Durée | 60 secondes |
| Objectif | Identifier le point de saturation |

### Métriques collectées
- Requêtes par seconde (throughput)
- Temps de réponse sous différentes charges
- Point de rupture (quand les erreurs commencent)

---

## Scénario 3 : Test de résilience (panne)

### Description
Observer le comportement de chaque méthode lorsque le service-voiture est indisponible.

### Étapes
1. Démarrer tous les services
2. Exécuter des requêtes de test
3. Arrêter le service-voiture
4. Observer les erreurs et délais
5. Redémarrer le service-voiture
6. Mesurer le temps de reprise

### Métriques collectées
- Délai avant détection de la panne
- Message d'erreur retourné
- Temps de reprise après redémarrage
- Comportement du fallback (Feign uniquement)

---

## Scénario 4 : Comparaison Discovery (Eureka vs Consul)

### Description
Comparer l'impact du système de discovery sur les performances.

### Configuration
Exécuter le Scénario 1 deux fois :
1. Avec Eureka comme discovery
2. Avec Consul comme discovery

### Métriques collectées
- Temps de latence de résolution de service
- Stabilité des connexions
- Temps de propagation des changements d'état

---

## Environnement de test

### Machine de test (à compléter)
| Caractéristique | Valeur |
|-----------------|--------|
| OS | Windows 10/11 |
| CPU |  |
| RAM |  |
| JVM | Java 17 |

### Versions des composants
| Composant | Version |
|-----------|---------|
| Spring Boot | 3.2.0 |
| Spring Cloud | 2023.0.0 |
| JMeter | 5.6 |
