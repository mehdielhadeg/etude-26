# Analyse Attendue

## Introduction

Ce document présente les axes d'analyse attendus pour le TP de comparaison des méthodes de communication inter-services.

---

## 1. Quelle méthode donne la meilleure latence en charge ?

### Éléments à analyser

- **Latence moyenne** : Comparer les temps de réponse moyens pour chaque méthode
- **Distribution** : Analyser les percentiles (P90, P99) pour comprendre la variabilité
- **Impact de la charge** : Comment la latence évolue avec le nombre de threads

### Hypothèse attendue

> **WebClient** devrait offrir la meilleure latence sous forte charge grâce à son modèle non-bloquant qui libère les threads pendant l'attente I/O.

### Points à justifier

1. Expliquer pourquoi le modèle bloquant (RestTemplate, Feign) consomme un thread par requête
2. Décrire comment le modèle réactif (WebClient) permet de gérer plus de requêtes avec moins de threads
3. Identifier le point à partir duquel la différence devient significative

---

## 2. Le débit maximal observé pour chaque méthode ?

### Éléments à analyser

- **Throughput** : Requêtes par seconde pour chaque méthode
- **Point de saturation** : Charge à partir de laquelle les performances dégradent
- **Taux d'erreur** : À quel moment les erreurs commencent à apparaître

### Résultats typiques attendus

| Méthode | Débit relatif | Explication |
|---------|---------------|-------------|
| RestTemplate | Baseline | Thread pool limité |
| Feign | ~= RestTemplate | Même modèle bloquant |
| WebClient | > RestTemplate | Event loop non-bloquant |

### Points à justifier

1. La taille du thread pool par défaut de Tomcat (200 threads)
2. L'impact du context switching sous forte charge
3. Les avantages du modèle réactif pour les applications I/O bound

---

## 3. Quelle méthode est la plus simple à maintenir ?

### Critères d'évaluation

| Critère | RestTemplate | Feign | WebClient |
|---------|--------------|-------|-----------|
| Lisibilité du code | Moyenne | Excellente | Moyenne |
| Courbe d'apprentissage | Faible | Faible | Élevée |
| Verbosité | Élevée | Faible | Moyenne |
| Testabilité | Bonne | Excellente | Complexe |
| Debug | Facile | Facile | Difficile |

### Analyse attendue

**Feign** est généralement considéré comme le plus maintenable pour ces raisons :
- Interface déclarative (annotation-driven)
- Intégration native avec Spring Cloud
- Fallback intégré pour la résilience
- Code minimal et auto-documenté

**RestTemplate** est le plus simple à comprendre mais :
- Deprecated depuis Spring 5
- Code verbeux
- Pas de support natif du load balancing

**WebClient** est le plus puissant mais :
- Paradigme réactif complexe à maîtriser
- Stack traces difficiles à lire
- Debugging non trivial

---

## 4. Impact du Discovery (Eureka vs Consul)

### Comparaison technique

| Aspect | Eureka | Consul |
|--------|--------|--------|
| **Architecture** | Serveur Java | Agent Go léger |
| **Health Check** | Client-side (heartbeat) | Agent-side (proactif) |
| **Latence discovery** | ~30s par défaut | ~10s configurable |
| **Multi-datacenter** | Non natif | Natif |
| **KV Store** | Non | Oui |

### Impact sur la latence

- **Eureka** : Latence légèrement plus élevée due au protocole HTTP uniquement
- **Consul** : Support DNS + HTTP + gRPC, résolution plus rapide

### Impact sur la stabilité

- **Eureka** : Self-preservation mode peut garder des services morts enregistrés
- **Consul** : Health checks agent-side détectent les pannes plus rapidement

### Analyse attendue

La différence de latence entre Eureka et Consul devrait être minime (quelques ms) dans un environnement de développement local. Les avantages de Consul sont plus visibles dans :
- Les environnements multi-datacenter
- Les scénarios de failover rapide
- L'utilisation du KV store pour la configuration

---

## 5. Comportement pendant une panne

### Scénarios à analyser

1. **Service voiture arrêté** : Que se passe-t-il ?
2. **Temps de détection** : Combien de temps avant que le discovery le détecte ?
3. **Reprise** : Combien de temps après redémarrage pour être à nouveau accessible ?

### Comportement attendu par méthode

| Méthode | Comportement en cas de panne |
|---------|------------------------------|
| RestTemplate | Exception, timeout après délai configuré |
| Feign | Appel du fallback si configuré |
| WebClient | Exception réactive (Mono.error) |

### Points à observer

1. **Feign avec Fallback** : Doit retourner une réponse par défaut
2. **RestTemplate/WebClient** : Doivent retourner une erreur HTTP appropriée
3. **Temps de propagation** : Le discovery met du temps à retirer le service mort

---

## Conseils de rédaction (niveau étudiant)

### 1. Décrire ce qui a été mesuré

✅ **Bon exemple** :
> "Nous avons mesuré le temps de réponse moyen sur 1000 requêtes (10 threads × 100 itérations) avec un ramp-up de 5 secondes."

❌ **Mauvais exemple** :
> "Les tests montrent que WebClient est plus rapide."

### 2. Justifier les valeurs

✅ **Bon exemple** :
> "WebClient présente une latence moyenne de 45ms contre 78ms pour RestTemplate. Cette différence s'explique par le modèle non-bloquant qui permet de traiter jusqu'à 10× plus de requêtes concurrentes avec le même nombre de threads."

❌ **Mauvais exemple** :
> "WebClient est meilleur."

### 3. Comparer et conclure

**Structure recommandée** :

1. **Tableau comparatif** avec les valeurs mesurées
2. **Observation** des différences principales
3. **Explication** technique de ces différences
4. **Recommandation** selon le contexte d'utilisation

### 4. Forces et faiblesses

| Méthode | Forces | Faiblesses | Cas d'usage recommandé |
|---------|--------|------------|------------------------|
| RestTemplate | Simple, bien documenté | Bloquant, deprecated | Migrations legacy |
| Feign | Déclaratif, lisible, fallback | Bloquant | Microservices Spring |
| WebClient | Performant, non-bloquant | Complexe | Haute performance |

---

## Conclusion type

> "Ce TP démontre que le choix d'une méthode de communication dépend du contexte :
> - **Feign** pour la majorité des cas grâce à sa simplicité et son intégration Spring Cloud
> - **WebClient** pour les applications nécessitant une haute performance sous forte charge
> - **RestTemplate** uniquement pour la maintenance de code existant
>
> Concernant le discovery, **Consul** offre des avantages en production (health checks, multi-DC) mais **Eureka** reste pertinent pour un environnement Spring Cloud homogène."
