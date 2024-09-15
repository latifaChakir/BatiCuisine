CREATE TABLE clients (
                         id SERIAL PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         adresse VARCHAR(255),
                         telephone VARCHAR(20),
                         estProfessionnel BOOLEAN NOT NULL
);
CREATE TYPE etatProjet AS ENUM ('ENCours', 'TERMINE', 'ANNULE');
CREATE TABLE projets (
                         id SERIAL PRIMARY KEY,
                         nomProjet VARCHAR(255) NOT NULL,
                         margeBeneficiaire DOUBLE PRECISION,
                         coutTotal DOUBLE PRECISION,
                         etat etatProjet,
                         client_id INT,
                         FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);
CREATE TYPE typeComposant AS ENUM ('Materiel', 'MainDOeuvre');
CREATE TABLE composants (
                            id SERIAL PRIMARY KEY,
                            nom VARCHAR(255) NOT NULL,
                            typeComposant typeComposant,
                            tauxTVA DOUBLE PRECISION
);
CREATE TABLE materiaux (
                           id SERIAL PRIMARY KEY,
                           composant_id INT,
                           coutUnitaire DOUBLE PRECISION,
                           quantite DOUBLE PRECISION,
                           coutTransport DOUBLE PRECISION,
                           coefficientQualite DOUBLE PRECISION,
                           FOREIGN KEY (composant_id) REFERENCES composants(id) ON DELETE CASCADE
);
CREATE TABLE main_oeuvre (
                             id SERIAL PRIMARY KEY,
                             composant_id INT,
                             tauxHoraire DOUBLE PRECISION,
                             heuresTravail DOUBLE PRECISION,
                             productiviteOuvrier DOUBLE PRECISION,
                             FOREIGN KEY (composant_id) REFERENCES composants(id) ON DELETE CASCADE
);
CREATE TABLE devis (
                       id SERIAL PRIMARY KEY,
                       montantEstime DOUBLE PRECISION,
                       dateEmission DATE,
                       dateValidite DATE,
                       accepte BOOLEAN,
                       projet_id INT,
                       FOREIGN KEY (projet_id) REFERENCES projets(id) ON DELETE CASCADE
);
CREATE TABLE remises (
                         id SERIAL PRIMARY KEY,
                         client_id INT,
                         tauxRemise DOUBLE PRECISION,
                         FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);
CREATE TABLE ajustements (
                             id SERIAL PRIMARY KEY,
                             composant_id INT,
                             typeAjustement VARCHAR(255), -- ex: 'Qualite', 'Productivite'
                             valeurAjustement DOUBLE PRECISION,
                             FOREIGN KEY (composant_id) REFERENCES composants(id) ON DELETE CASCADE
);
