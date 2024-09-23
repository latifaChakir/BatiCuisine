# Bati-Cuisine: Application d'Estimation des Coûts de Construction de Cuisines
### Description
Bati-Cuisine est une application Java destinée aux professionnels de la construction et de la rénovation de cuisines. Elle permet de calculer le coût total des travaux en tenant compte des matériaux utilisés et du coût de la main-d'œuvre, tout en offrant des fonctionnalités avancées de gestion de projets.

## Fonctionnalités principales

### 1. Gestion des Projets

Ajout de clients associés aux projets
Gestion des composants (matériaux, main-d'œuvre)
Association de devis aux projets


### 2. Gestion des Composants

Matériaux : coûts, quantités, qualité
Main-d'œuvre : taux horaires, heures travaillées, productivité


### 3. Gestion des Clients

Enregistrement des informations client
Différenciation entre clients professionnels et particuliers
Calcul de remises spécifiques


### 4. Création de Devis

Génération de devis détaillés
Gestion des dates d'émission et de validité
Suivi de l'acceptation des devis

## Exigences techniques

- Java 8.
- PostgreSQL.
- JDBC.
- Streams, Collections, HashMap, Optional.
- Enum.
- Design patterns : Singleton, Repository Pattern.
- Architecture en couches (Service, DAO, UI).
- Validation des données.
- Java Time API.
- Git et JIRA.
  
## Structure de projet
<img width="222" alt="structure" src="https://github.com/user-attachments/assets/121493d3-295b-4bec-92fd-1908704291f8">

Le projet Bati-Cuisine est structuré selon une architecture en couches, facilitant la séparation des responsabilités et améliorant la maintenabilité du code. Voici une description de chaque composant :

- **src:** Source principale du projet contient tout le code source et les ressources.
- **db:** Base de données
- **migration:** scripts de migration de la base de données
- **V1__initial_tables.sql:** script initial pour créer les tables de la base de données.
- **main/java:** Code source principal contient les packages Java du projet.
- **bean:** Objets métier contient les classes Java représentant les entités métier (par exemple, Client, Projet, Matériau).
- **config:** Configuration gère la configuration de l'application, potentiellement incluant la connexion à la base de données.
- **dao:** Data Access Object contient les classes responsables de l'interaction avec la base de données.
- **exceptions:** Gestion des exceptions définit des exceptions personnalisées pour gérer les erreurs spécifiques à l'application.
- **service:** Couche de service Implémente la logique métier de l'application, faisant le lien entre l'interface utilisateur et la couche DAO.
- **ui:** Interface utilisateur gère l'interaction avec l'utilisateur via une interface console.
- **utils:** utilitaires contient des classes utilitaires réutilisables dans l'ensemble du projet.
- **Main.java:** Point d'entrée, classe principale contenant la méthode main() pour démarrer l'application.

Cette architecture suit les principes de conception modernes, favorisant la modularité, la réutilisabilité et la facilité de maintenance du code. La séparation claire entre les différentes couches (UI, Service, DAO) permet une meilleure gestion des responsabilités.
## Installation : 
Cloner le dépôt Git :
```bash
  git clone https://github.com/latifaChakir/BatiCuisine.git
```
Installer PostgreSQL et créer une base de données pour le projet.
Exécuter les scripts SQL fournis pour initialiser la base de données.
Configurer les paramètres de connexion à la base de données dans le fichier de configuration de l'application.
Compiler le projet et générer le fichier JAR :

```bash
  mvn clean package
```
## Utilisation : 
Lancer l'application :
```bash
  java  -jar bati-cuisine.jar
```
Suivre les instructions à l'écran pour naviguer dans le menu principal et utiliser les différentes fonctionnalités.
