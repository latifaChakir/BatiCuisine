package ui;

import bean.*;
import bean.enums.EtatProjet;
import exceptions.ClientValidationException;
import exceptions.ProjectValidationException;
import service.ClientService;
import service.ComposantService;
import service.DevisService;
import service.ProjetService;
import utils.Validations;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjetMenu {
    private ProjetService projetService;
    private DevisService devisService;
    private ComposantService composantService;
    private ClientMenu clientMenu;
    private Scanner scanner;
    private DevisMenu devisMenu;

    public ProjetMenu(ProjetService projetService, DevisService devisService, ComposantService composantService, ClientMenu clientMenu, Scanner scanner) {
        this.projetService = projetService;
        this.devisService = devisService;
        this.composantService = composantService;
        this.clientMenu = clientMenu;
        this.scanner = scanner;
        this.devisMenu = new DevisMenu(devisService, scanner);
    }

    private Projet inputsProjet() {
        Client client = null;

        while (client == null) {
            System.out.println("Voulez-vous associer ce projet à un client existant ou créer un nouveau client ?");
            System.out.println("1. \uD83D\uDD0D Chercher un client existant");
            System.out.println("2. ➕ Ajouter un nouveau client");
            System.out.print("Choisir une option: ");
            int choixClient;
            try {
                choixClient = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                continue;
            }
            switch (choixClient) {
                case 1:
                    client = searchClientForProject();
                    break;
                case 2:
                    client = addNewClientForProject();
                    break;
                case 3:
                        return null;
                default:
                    System.out.println("Option non valide. Veuillez réessayer.");
            }
        }

        System.out.print("Nom du projet : ");
        String nomProjet = scanner.nextLine();
        double surface = -1;
        while (surface <= 0) {
            System.out.print("Surface : ");
            String surfaceInput = scanner.nextLine();
            if (surfaceInput.isEmpty()) {
                System.out.println("La surface ne peut pas être vide. Veuillez entrer une valeur valide.");
            } else {
                try {
                    surface = Double.parseDouble(surfaceInput);
                    if (surface <= 0) {
                        System.out.println("La surface doit être un nombre positif.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrée non valide. Veuillez entrer un nombre valide pour la surface.");
                }
            }
        }
        EtatProjet etat = null;
        boolean etatValide = false;

        while (!etatValide ) {
            System.out.print("État du projet (par ex: ENCOURS, TERMINE, ANNULE) : ");
            String etatInput = scanner.nextLine().toUpperCase();

            try {
                etat = EtatProjet.valueOf(etatInput);
                etatValide = true;
            } catch (IllegalArgumentException e) {
                System.out.println("L'état saisi n'existe pas. Veuillez entrer un état valide (ENCOURS, TERMINE, ANNULE).");
            }
        }


        Projet projet =new Projet(nomProjet, 0, 0, etat, client, surface);
        try {
            Validations.projetValidation(projet);
        } catch (ProjectValidationException e) {
            System.err.println(e.getMessage());
            System.err.println("Le projet n'a pas été ajouté en raison d'erreurs de validation.");
            System.out.println();
            return null;
//            principalMenu.principalMenu();
        }
        return projet;
    }

    private Client searchClientForProject() {
        System.out.print("Entrer le nom du client : ");
        String name = scanner.nextLine();
        List<Client> clientsByName = clientMenu.findClientByName(name);

        if (!clientsByName.isEmpty()) {
            System.out.print("Sélectionnez un client par ID ou tapez '0' pour annuler.");

            int clientId = Integer.parseInt(scanner.nextLine());
            if (clientId == 0) {
                System.out.println("Opération annulée.");
                return null;
            }

            for (Client client : clientsByName) {
                if (client.getId() == clientId) {
                    return client;
                }
            }

            System.out.println("Client avec l'ID " + clientId + " non trouvé dans la liste.");
        } else {
            System.out.println("Client non trouvé.");
        }

        return null;
    }

    private void handleSaveProject(Projet projet, Client client) {
        if (client.getId() == 0) {
            projetService.saveProjetClient(projet, client);
        } else {
            projetService.saveProjetClientFound(projet, client);
        }
    }

    private Client addNewClientForProject() {
        return clientMenu.addNewClient();
    }

    public void deleteProjetById() {
        System.out.print("Entrer l'ID du projet à supprimer : ");
        int projectId = Integer.parseInt(scanner.nextLine());
        projetService.delete(projectId);
    }

    public void findProjetById() {
        System.out.print("Entrer l'ID du projet : ");
        int projectId = Integer.parseInt(scanner.nextLine());
        Optional<Projet> projetOpt = projetService.findById(projectId);

        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();

            System.out.println("--------------------------------------------------Détail du Projet " + projet.getNomProjet() + "--------------------------------------------------");
            System.out.println("✨ ID: " + projet.getId());
            System.out.println("✨ Nom de projet: " + projet.getNomProjet());
            System.out.println("✨ État de projet: " + projet.getEtat());
            System.out.println("✨ Surface de projet: " + projet.getSurface());

            System.out.println("---- Détails du Client \uD83E\uDDD1 ----");
            Client client = projet.getClient();
            if (client != null) {
                System.out.println("✨ ID: " + client.getId());
                System.out.println("✨ Nom: " + client.getNom());
                System.out.println("✨ Adresse: " + client.getAdresse());
                System.out.println("✨ Téléphone: " + client.getTelephone());
            } else {
                System.out.println("Aucun client associé à ce projet.");
            }

            // Initialize total costs
            double totalCoutMateriaux = 0.0;
            double totalCoutMateriauxAvecTVA = 0.0;
            double totalCoutMainOeuvre = 0.0;
            double totalCoutMainOeuvreAvecTVA = 0.0;

            boolean hasMaterials = false;
            boolean hasMainOeuvre = false;

            System.out.println("---- Détails des Composants du Projet \uD83D\uDD75\uFE0F ----");
            for (Composant composant : projet.getComposants()) {
                if (composant.getMateriaux() != null && !composant.getMateriaux().isEmpty()) {
                    if (!hasMaterials) {
                        System.out.println("1. Matériaux :");
                        hasMaterials = true;
                    }
                    for (Materiau materiau : composant.getMateriaux()) {
                        double coutMateriauBrute = materiau.getQuantite() * materiau.getCoutUnitaire();
                        double coutMateriauAjuste = coutMateriauBrute * materiau.getCoefficientQualite();
                        double coutMateriauTotal = coutMateriauAjuste + materiau.getCoutTransport();
                        totalCoutMateriaux += coutMateriauTotal;

                        double tva = composant.getTauxTVA() / 100;
                        double coutMateriauTotalAvecTVA = coutMateriauTotal * (1 + tva);
                        totalCoutMateriauxAvecTVA += coutMateriauTotalAvecTVA;

                        System.out.printf("- %s : %.2f DH (quantité : %.2f, coût unitaire : %.2f DH/unité, qualité : %.2f, transport : %.2f DH, total avec TVA : %.2f DH)\n",
                                composant.getNom(),
                                coutMateriauTotal,
                                materiau.getQuantite(),
                                materiau.getCoutUnitaire(),
                                materiau.getCoefficientQualite(),
                                materiau.getCoutTransport(),
                                coutMateriauTotalAvecTVA);
                    }
                }
            }

            if (hasMaterials) {
                System.out.printf("\uD83D\uDCB0 Coût total des matériaux avant TVA : %.2f DH\n", totalCoutMateriaux);
                System.out.printf("\uD83D\uDCB0 Coût total des matériaux avec TVA  : %.2f DH\n", totalCoutMateriauxAvecTVA);
            }

            // Process labor
            for (Composant composant : projet.getComposants()) {
                if (composant.getMainOeuvres() != null && !composant.getMainOeuvres().isEmpty()) {
                    if (!hasMainOeuvre) {
                        System.out.println("2. Main-d'œuvre :");
                        hasMainOeuvre = true;
                    }
                    for (MainOeuvre mainOeuvre : composant.getMainOeuvres()) {
                        double coutMainOeuvre = mainOeuvre.getTauxHoraire() * mainOeuvre.getHeuresTravail();
                        totalCoutMainOeuvre += coutMainOeuvre * mainOeuvre.getProductiviteOuvrier();

                        double tva = composant.getTauxTVA() / 100;
                        double coutMainOeuvreAvecTVA = coutMainOeuvre * (1 + tva);
                        totalCoutMainOeuvreAvecTVA += coutMainOeuvreAvecTVA;

                        System.out.printf("- %s : %.2f DH (taux horaire : %.2f DH/h, heures travaillées : %.2f h, productivité : %.1f, total avec TVA : %.2f DH)\n",
                                composant.getNom(),
                                coutMainOeuvre,
                                mainOeuvre.getTauxHoraire(),
                                mainOeuvre.getHeuresTravail(),
                                mainOeuvre.getProductiviteOuvrier(),
                                coutMainOeuvreAvecTVA);
                    }
                }
            }

            if (hasMainOeuvre) {
                System.out.printf("\uD83D\uDCB0 Coût total de la main-d'œuvre avant TVA : %.2f DH\n", totalCoutMainOeuvre);
                System.out.printf("\uD83D\uDCB0 Coût total de la main-d'œuvre avec TVA : %.2f DH\n", totalCoutMainOeuvreAvecTVA);
                System.out.println("--------------------------------------------------FIN de Détail du Projet--------------------------------------------------");
            }

        } else {
            System.out.println("Projet non trouvé.");
        }
    }

    public void createProject() {
        Projet projet = inputsProjet();
        if(projet != null) {
            handleSaveProject(projet, projet.getClient());
        }else {
            System.err.println("Le projet n'a pas été ajouté en raison d'erreurs de validation.");
        }
        System.out.println("Voulez-vous ajouter des composants à ce projet ? (oui/non)");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("oui")) {
            ComposantMenu composantMenu = new ComposantMenu(new ComposantService(),scanner);
            composantMenu.ajouterComposantAuProjet(projet);
        }
    }
    private List<Composant> findByProject(Projet projet){
        List<Composant> composants = composantService.findByProject(projet);
        if (!composants.isEmpty()) {
//            for (Composant c : composants) {
//                System.out.println(c);
//            }
        } else {
            System.out.println("Aucun composant trouvé.");
        }
        return composants;
    }

    public void updateProject() {
        System.out.print("Entrer l'ID du projet à mettre à jour : ");
        int projectId = Integer.parseInt(scanner.nextLine());

        Optional<Projet> projetOptional = projetService.findById(projectId);
        if (projetOptional.isPresent()) {
            Projet projet = projetOptional.get();
            List<Composant> composants = findByProject(projet);
            boolean materiauExisteDeja = false;

            for (Composant c : composants) {
                materiauExisteDeja = true;
                break;
            }

            if (materiauExisteDeja) {
                composantService.supprimerComposantsParProjet(projet);
            }
            System.out.println("Projet trouvé : ");
            System.out.println("✨ ID: " + projet.getId());
            System.out.println("✨ Nom de projet: " + projet.getNomProjet());
            System.out.println("✨ État de projet: " + projet.getEtat());
            System.out.println("✨ Surface de projet: " + projet.getSurface());//////0 VOIR
            System.out.println("---- Détails du Client \uD83E\uDDD1 ----");
            Client client = projet.getClient();
            if (client != null) {
                System.out.println("✨ ID: " + client.getId());
                System.out.println("✨ Nom: " + client.getNom());
                System.out.println("✨ Adresse: " + client.getAdresse());
                System.out.println("✨ Téléphone: " + client.getTelephone());
            } else {
                System.out.println("Aucun client associé à ce projet.");
            }

            Projet updatedProjet = inputsToUpdate(projet);
            updatedProjet.setId(projectId);
            projetService.update(updatedProjet);

            System.out.println("Voulez-vous modifier les composants de ce projet ? (oui/non)");
            String reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("oui")) {
                ComposantMenu composantMenu = new ComposantMenu(new ComposantService(),scanner);
                composantMenu.updateComposantduProjet(updatedProjet);
            }

            System.out.println("Projet et composants mis à jour avec succès.");
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

    public void findAllProject() {
        projetService.findAll().forEach(projet -> {
            System.out.println("--------------------------------------------------Détail du Projet "+projet.getNomProjet()+"--------------------------------------------------");
            System.out.println("ID de projet: " + projet.getId());
            System.out.println("Nom de projet: " + projet.getNomProjet());
            System.out.println("État de projet: " + projet.getEtat());

            System.out.println("---- Détails du Client ----");
            Client client = projet.getClient();
            if (client != null) {
                System.out.println("ID de client: " + client.getId());
                System.out.println("Nom: " + client.getNom());
                System.out.println("Adresse: " + client.getAdresse());
                System.out.println("Téléphone: " + client.getTelephone());
            }

            System.out.println("---- Détails des Composants du Projet ----");

            double totalCoutMateriaux = 0.0;
            double totalCoutMateriauxAvecTVA = 0.0;
            double totalCoutMainOeuvre = 0.0;
            double totalCoutMainOeuvreAvecTVA = 0.0;

            boolean hasMaterials = false;
            boolean hasMainOeuvre = false;

            // Traitement des matériaux
            for (Composant composant : projet.getComposants()) {
                if (composant.getMateriaux() != null && !composant.getMateriaux().isEmpty()) {
                    if (!hasMaterials) {
                        System.out.println("1. Matériaux :");
                        hasMaterials = true;
                    }
                    for (Materiau materiau : composant.getMateriaux()) {
                        double coutMateriauBrute = materiau.getQuantite() * materiau.getCoutUnitaire();
                        double coutMateriauAjuste = coutMateriauBrute * materiau.getCoefficientQualite();
                        double coutMateriauTotal = coutMateriauAjuste + materiau.getCoutTransport();
                        totalCoutMateriaux += coutMateriauTotal;

                        double tva = composant.getTauxTVA() / 100;
                        double coutMateriauTotalAvecTVA = coutMateriauTotal * (1 + tva);
                        totalCoutMateriauxAvecTVA += coutMateriauTotalAvecTVA;

                        System.out.printf("- %s : %.2f DH (quantité : %.2f, coût unitaire : %.2f DH/unité, qualité : %.2f, transport : %.2f DH, total avec TVA : %.2f DH)\n",
                                composant.getNom(),
                                coutMateriauTotal,
                                materiau.getQuantite(),
                                materiau.getCoutUnitaire(),
                                materiau.getCoefficientQualite(),
                                materiau.getCoutTransport(),
                                coutMateriauTotalAvecTVA);
                    }
                }
            }

            if (hasMaterials) {
                System.out.printf("**Coût total des matériaux avant TVA : %.2f DH**\n", totalCoutMateriaux);
                System.out.printf("**Coût total des matériaux avec TVA  : %.2f DH**\n", totalCoutMateriauxAvecTVA);
            }

            // Traitement de la main-d'œuvre
            for (Composant composant : projet.getComposants()) {
                if (composant.getMainOeuvres() != null && !composant.getMainOeuvres().isEmpty()) {
                    if (!hasMainOeuvre) {
                        System.out.println("2. Main-d'œuvre :");
                        hasMainOeuvre = true;
                    }
                    for (MainOeuvre mainOeuvre : composant.getMainOeuvres()) {
                        double coutMainOeuvre = mainOeuvre.getTauxHoraire() * mainOeuvre.getHeuresTravail();
                        totalCoutMainOeuvre += coutMainOeuvre * mainOeuvre.getProductiviteOuvrier();;

                        double tva = composant.getTauxTVA() / 100;
                        double coutMainOeuvreAvecTVA = coutMainOeuvre * (1 + tva);
                        totalCoutMainOeuvreAvecTVA += coutMainOeuvreAvecTVA;

                        System.out.printf("- %s : %.2f DH (taux horaire : %.2f DH/h, heures travaillées : %.2f h, productivité : %.1f, total avec TVA : %.2f DH)\n",
                                composant.getNom(),
                                coutMainOeuvre,
                                mainOeuvre.getTauxHoraire(),
                                mainOeuvre.getHeuresTravail(),
                                mainOeuvre.getProductiviteOuvrier(),
                                coutMainOeuvreAvecTVA);
                    }
                }
            }

            if (hasMainOeuvre) {
                System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f DH**\n", totalCoutMainOeuvre);
                System.out.printf("**Coût total de la main-d'œuvre avec TVA : %.2f DH**\n", totalCoutMainOeuvreAvecTVA);
            }

            System.out.println("--- Fin des Détails du Projet ---\n");
        });
    }

    public void findByName(){
        System.out.print("Entrez le nom du projet à rechercher : ");
        String nomProjet = scanner.nextLine();
        List<Projet> projets = projetService.findByName(nomProjet);
        if (!projets.isEmpty()) {
            projets.stream().sorted(Comparator.comparing(Projet::getNomProjet))
                    .forEach(projet -> {
                                System.out.println("--------------------------------------------------Détail du Projet " + projet.getNomProjet() + "--------------------------------------------------");
                                System.out.println("✨ ID: " + projet.getId());
                                System.out.println("✨ Nom de projet: " + projet.getNomProjet());
                                System.out.println("✨ État de projet: " + projet.getEtat());
                                System.out.println("✨ Surface de projet: " + projet.getSurface());

                                System.out.println("---- Détails du Client \uD83E\uDDD1 ----");
                                Client client = projet.getClient();
                                if (client != null) {
                                    System.out.println("✨ ID: " + client.getId());
                                    System.out.println("✨ Nom: " + client.getNom());
                                    System.out.println("✨ Adresse: " + client.getAdresse());
                                    System.out.println("✨ Téléphone: " + client.getTelephone());
                                } else {
                                    System.out.println("Aucun client associé à ce projet.");
                                }

                                // Initialize total costs
                                double totalCoutMateriaux = 0.0;
                                double totalCoutMateriauxAvecTVA = 0.0;
                                double totalCoutMainOeuvre = 0.0;
                                double totalCoutMainOeuvreAvecTVA = 0.0;

                                boolean hasMaterials = false;
                                boolean hasMainOeuvre = false;

                                System.out.println("---- Détails des Composants du Projet \uD83D\uDD75\uFE0F ----");
                                for (Composant composant : projet.getComposants()) {
                                    if (composant.getMateriaux() != null && !composant.getMateriaux().isEmpty()) {
                                        if (!hasMaterials) {
                                            System.out.println("1. Matériaux :");
                                            hasMaterials = true;
                                        }
                                        for (Materiau materiau : composant.getMateriaux()) {
                                            double coutMateriauBrute = materiau.getQuantite() * materiau.getCoutUnitaire();
                                            double coutMateriauAjuste = coutMateriauBrute * materiau.getCoefficientQualite();
                                            double coutMateriauTotal = coutMateriauAjuste + materiau.getCoutTransport();
                                            totalCoutMateriaux += coutMateriauTotal;

                                            double tva = composant.getTauxTVA() / 100;
                                            double coutMateriauTotalAvecTVA = coutMateriauTotal * (1 + tva);
                                            totalCoutMateriauxAvecTVA += coutMateriauTotalAvecTVA;

                                            System.out.printf("- %s : %.2f DH (quantité : %.2f, coût unitaire : %.2f DH/unité, qualité : %.2f, transport : %.2f DH, total avec TVA : %.2f DH)\n",
                                                    composant.getNom(),
                                                    coutMateriauTotal,
                                                    materiau.getQuantite(),
                                                    materiau.getCoutUnitaire(),
                                                    materiau.getCoefficientQualite(),
                                                    materiau.getCoutTransport(),
                                                    coutMateriauTotalAvecTVA);
                                        }
                                    }
                                }

                                if (hasMaterials) {
                                    System.out.printf("\uD83D\uDCB0 Coût total des matériaux avant TVA : %.2f DH\n", totalCoutMateriaux);
                                    System.out.printf("\uD83D\uDCB0 Coût total des matériaux avec TVA  : %.2f DH\n", totalCoutMateriauxAvecTVA);
                                }

                                // Process labor
                                for (Composant composant : projet.getComposants()) {
                                    if (composant.getMainOeuvres() != null && !composant.getMainOeuvres().isEmpty()) {
                                        if (!hasMainOeuvre) {
                                            System.out.println("2. Main-d'œuvre :");
                                            hasMainOeuvre = true;
                                        }
                                        for (MainOeuvre mainOeuvre : composant.getMainOeuvres()) {
                                            double coutMainOeuvre = mainOeuvre.getTauxHoraire() * mainOeuvre.getHeuresTravail();
                                            totalCoutMainOeuvre += coutMainOeuvre * mainOeuvre.getProductiviteOuvrier();

                                            double tva = composant.getTauxTVA() / 100;
                                            double coutMainOeuvreAvecTVA = coutMainOeuvre * (1 + tva);
                                            totalCoutMainOeuvreAvecTVA += coutMainOeuvreAvecTVA;

                                            System.out.printf("- %s : %.2f DH (taux horaire : %.2f DH/h, heures travaillées : %.2f h, productivité : %.1f, total avec TVA : %.2f DH)\n",
                                                    composant.getNom(),
                                                    coutMainOeuvre,
                                                    mainOeuvre.getTauxHoraire(),
                                                    mainOeuvre.getHeuresTravail(),
                                                    mainOeuvre.getProductiviteOuvrier(),
                                                    coutMainOeuvreAvecTVA);
                                        }
                                    }
                                }

                                if (hasMainOeuvre) {
                                    System.out.printf("\uD83D\uDCB0 Coût total de la main-d'œuvre avant TVA : %.2f DH\n", totalCoutMainOeuvre);
                                    System.out.printf("\uD83D\uDCB0 Coût total de la main-d'œuvre avec TVA : %.2f DH\n", totalCoutMainOeuvreAvecTVA);


                                }
                            }
                            );
        }
    }

    private Projet inputsToUpdate(Projet projet) {
        Client client = projet.getClient();
        System.out.print("Voulez-vous modifier le client associé ? (o/n): ");
        String choix = scanner.nextLine().trim().toLowerCase();
        if (choix.equalsIgnoreCase("o")) {
            clientMenu.updateClient();
        }
        projet.setClient(client);
        System.out.print("Nom du projet (actuel: " + projet.getNomProjet() + ") : ");
        String nomProjet = scanner.nextLine();
        if (!nomProjet.trim().isEmpty()) {
            projet.setNomProjet(nomProjet);
        }

        double surface = projet.getSurface();
        System.out.print("Surface (actuelle: " + projet.getSurface() + ") : ");
        String surfaceInput = scanner.nextLine();
        if (!surfaceInput.trim().isEmpty()) {
            try {
                surface = Double.parseDouble(surfaceInput);
                if (surface > 0) {
                    projet.setSurface(surface);
                } else {
                    System.out.println("La surface doit être un nombre positif.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée non valide. Veuillez entrer un nombre valide pour la surface.");
            }
        }

        EtatProjet etat = projet.getEtat();
        boolean etatValide = false;

        while (!etatValide) {
            System.out.print("État du projet (actuel: " + projet.getEtat() + ", par ex: ENCOURS, TERMINE, ANNULE) : ");
            String etatInput = scanner.nextLine().toUpperCase();
            if (!etatInput.trim().isEmpty()) {
                try {
                    etat = EtatProjet.valueOf(etatInput);
                    etatValide = true;
                    projet.setEtat(etat);
                } catch (IllegalArgumentException e) {
                    System.out.println("L'état saisi n'existe pas. Veuillez entrer un état valide (ENCOURS, TERMINE, ANNULE).");
                }
            } else {
                etatValide = true;
            }
        }

        return projet;
    }





}
