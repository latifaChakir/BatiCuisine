package ui;

import bean.*;
import bean.enums.EtatProjet;
import service.ClientService;
import service.ComposantService;
import service.ProjetService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjetMenu {
    private ComposantService composantService;
    private ProjetService projetService;
    private ClientMenu clientMenu;
    private CalculTotalMenu calculTotalMenu;
    private static Scanner scanner;

    public ProjetMenu(ProjetService projetService) {
        this.projetService = projetService;
        this.composantService=new ComposantService();
        this.calculTotalMenu=new CalculTotalMenu();
        this.clientMenu = new ClientMenu(new ClientService());
        scanner = new Scanner(System.in);
    }


    private Projet inputsProjet() {
        Client client = null;

        while (client == null) {
            System.out.println("Voulez-vous associer ce projet à un client existant ou créer un nouveau client ?");
            System.out.println("1. Chercher un client existant");
            System.out.println("2. Ajouter un nouveau client");
            System.out.print("Choisir une option: ");

            int choixClient = Integer.parseInt(scanner.nextLine());

            switch (choixClient) {
                case 1:
                    client = searchClientForProject();
                    break;
                case 2:
                    client = addNewClientForProject();
                    break;
                default:
                    System.out.println("Option non valide. Veuillez réessayer.");
            }
        }

        System.out.print("Nom du projet : ");
        String nomProjet = scanner.nextLine();
        System.out.print("Surface : ");
        double surface = Double.parseDouble(scanner.nextLine());

        System.out.print("État du projet (par ex: ENCOURS, TERMINE, ANNULE) : ");
        String etatInput = scanner.nextLine().toUpperCase();
        EtatProjet etat = EtatProjet.valueOf(etatInput);

        return new Projet(nomProjet, 0, 0, etat, client, surface);
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

    private void deleteProjetById() {
        System.out.print("Entrer l'ID du projet à supprimer : ");
        int projectId = Integer.parseInt(scanner.nextLine());
        projetService.delete(projectId);
        System.out.println("Projet supprimé.");
    }

    private void findProjetById() {
        System.out.print("Entrer l'ID du projet : ");
        int projectId = Integer.parseInt(scanner.nextLine());
        Optional<Projet> projet = projetService.findById(projectId);
        if (projet.isPresent()) {
            System.out.println(projet.get());
        } else {
            System.out.println("Projet non trouvé.");
        }
    }


    private int getProjetIdInput() {
        System.out.print("Entrer l'ID du projet : ");
        return Integer.parseInt(scanner.nextLine());
    }

    public void createProject() {
        Projet projet = inputsProjet();
        handleSaveProject(projet, projet.getClient());
        System.out.println("Voulez-vous ajouter des composants à ce projet ? (oui/non)");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("oui")) {
            ComposantMenu composantMenu = new ComposantMenu(new ComposantService());
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
            System.out.println("Projet trouvé : " + projet);

            Projet updatedProjet = inputsProjet();
            updatedProjet.setId(projectId);

            projetService.update(updatedProjet);

            System.out.println("Voulez-vous modifier les composants de ce projet ? (oui/non)");
            String reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("oui")) {
                ComposantMenu composantMenu = new ComposantMenu(new ComposantService());
                composantMenu.updateComposantduProjet(updatedProjet);
            }

            System.out.println("Projet et composants mis à jour avec succès.");
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

    public void findAllProject() {
        projetService.findAll().forEach(projet -> {
            System.out.println("--- Détail du Projet ---");
            System.out.println("ID: " + projet.getId());
            System.out.println("Nom de projet: " + projet.getNomProjet());
            System.out.println("Surface: " + projet.getSurface());
            System.out.println("État de projet: " + projet.getEtat());
            System.out.println("Marge bénéficiaire: " + projet.getMargeBeneficiaire());
            System.out.println("Coût Total: " + projet.getCoutTotal());

            System.out.println("---- Détails du Client ----");
            Client client = projet.getClient();
            if (client != null) {
                System.out.println("ID: " + client.getId());
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

                        System.out.printf("- %s : %.2f € (quantité : %.2f, coût unitaire : %.2f €/unité, qualité : %.2f, transport : %.2f €, total avec TVA : %.2f €)\n",
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
                System.out.printf("**Coût total des matériaux avant TVA : %.2f €**\n", totalCoutMateriaux);
                System.out.printf("**Coût total des matériaux avec TVA  : %.2f €**\n", totalCoutMateriauxAvecTVA);
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
                        totalCoutMainOeuvre += coutMainOeuvre;

                        double tva = composant.getTauxTVA() / 100;
                        double coutMainOeuvreAvecTVA = coutMainOeuvre * (1 + tva);
                        totalCoutMainOeuvreAvecTVA += coutMainOeuvreAvecTVA;

                        System.out.printf("- %s : %.2f € (taux horaire : %.2f €/h, heures travaillées : %.2f h, productivité : %.1f, total avec TVA : %.2f €)\n",
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
                System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f €**\n", totalCoutMainOeuvre);
                System.out.printf("**Coût total de la main-d'œuvre avec TVA : %.2f €**\n", totalCoutMainOeuvreAvecTVA);
            }

            System.out.println("--- Fin des Détails du Projet ---\n");
        });
    }

    public void findByIdProject() {
        System.out.println("Entrez l'ID du projet que vous souhaitez trouver :");
        int projectId = Integer.parseInt(scanner.nextLine());

        Optional<Projet> projetOptional = projetService.findById(projectId);
        if (projetOptional.isPresent()) {
            Projet projet = projetOptional.get();
            System.out.println("--- Détail du Projet ---");
            System.out.println("ID: " + projet.getId());
            System.out.println("Nom de projet: " + projet.getNomProjet());
            System.out.println("Surface: " + projet.getSurface());
            System.out.println("État de projet: " + projet.getEtat());
            System.out.println("---- Détails du Client ----");
            Client client = projet.getClient();
            if (client != null) {
                System.out.println("ID: " + client.getId());
                System.out.println("Nom: " + client.getNom());
                System.out.println("Adresse: " + client.getAdresse());
                System.out.println("Téléphone: " + client.getTelephone());
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

                            System.out.printf("- %s : %.2f € (quantité : %.2f, coût unitaire : %.2f €/unité, qualité : %.2f, transport : %.2f €, total avec TVA : %.2f €)\n",
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
                    System.out.printf("**Coût total des matériaux avant TVA : %.2f €**\n", totalCoutMateriaux);
                    System.out.printf("**Coût total des matériaux avec TVA  : %.2f €**\n", totalCoutMateriauxAvecTVA);
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
                            totalCoutMainOeuvre += coutMainOeuvre;

                            double tva = composant.getTauxTVA() / 100;
                            double coutMainOeuvreAvecTVA = coutMainOeuvre * (1 + tva);
                            totalCoutMainOeuvreAvecTVA += coutMainOeuvreAvecTVA;

                            System.out.printf("- %s : %.2f € (taux horaire : %.2f €/h, heures travaillées : %.2f h, productivité : %.1f, total avec TVA : %.2f €)\n",
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
                    System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f €**\n", totalCoutMainOeuvre);
                    System.out.printf("**Coût total de la main-d'œuvre avec TVA : %.2f €**\n", totalCoutMainOeuvreAvecTVA);
                }

                System.out.println("--- Fin des Détails du Projet ---\n");
                System.out.println("--- Calcul en cours ---\n");
                calculTotalMenu.inputForCalculTotal(projet);
                double coutTotal = totalCoutMainOeuvre+totalCoutMateriaux;
                double coutTotalAvantMarge = totalCoutMainOeuvreAvecTVA+totalCoutMateriauxAvecTVA;
                double coutTotalAvecMarge = coutTotalAvantMarge * (1 + (projet.getMargeBeneficiaire()/100));

                System.out.println("total de projet avant marge : "+coutTotalAvantMarge);
                    System.out.println("Marge beneficaire : "+projet.getMargeBeneficiaire());
                System.out.println("total de projet final : " + coutTotalAvecMarge);

            }
        } else {
            System.out.println("Projet non trouvé.");
        }
    }
}
