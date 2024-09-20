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
    private static Scanner scanner;

    public ProjetMenu(ProjetService projetService) {
        this.projetService = projetService;
        this.composantService=new ComposantService();
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
            System.out.println("les détails de projet");
            System.out.println("ID: " + projet.getId());
            System.out.println("Nom de projet: " + projet.getNomProjet());
            System.out.println("Surface: " + projet.getSurface());
            System.out.println("Etat de projet: " + projet.getEtat());
            System.out.println("Marge beneficiaire: " + projet.getMargeBeneficiaire());
            System.out.println("Cout Total: " + projet.getCoutTotal());

            System.out.println("les détails de client de ce projet");
            Client client = projet.getClient();
            if (client != null) {
                System.out.println("ID: " + client.getId());
                System.out.println("Nom: " + client.getNom());
                System.out.println("Adresse: " + client.getAdresse());
                System.out.println("Téléphone: " + client.getTelephone());
            }

            System.out.println("Les détails des composants de ce projet");

            if (projet.getComposants() != null) {
                projet.getComposants().forEach(composant -> {
                    double totalCoutMateriaux = 0.0;
                    double totalCoutMainOeuvre = 0.0;

                    if (composant.getMateriaux() != null && !composant.getMateriaux().isEmpty()) {
                        System.out.println("****** Matériaux : ******");
                        for (Materiau materiau : composant.getMateriaux()) {
                            double coutMateriau = materiau.getQuantite() * materiau.getCoutUnitaire();
                            totalCoutMateriaux += coutMateriau + materiau.getCoutTransport();
                            System.out.printf("- %s : %.2f € (quantité : %.2f %s, coût unitaire : %.2f €/unité, transport : %.2f €, qualité : %.1f)\n",
                                    composant.getNom(),
                                    coutMateriau,
                                    materiau.getQuantite(),
                                    materiau.getCoutUnitaire(),
                                    materiau.getCoutUnitaire(),
                                    materiau.getCoutTransport(),
                                    materiau.getCoefficientQualite());
                        }
                        System.out.printf("**Coût total des matériaux avant TVA : %.2f €**\n", totalCoutMateriaux);
                        double tva = composant.getTauxTVA() / 100;
                        System.out.printf("**Coût total des matériaux avec TVA (%.0f%%) : %.2f €**\n",
                                composant.getTauxTVA(),
                                totalCoutMateriaux * (1 + tva));
                    }

                    if (composant.getMainOeuvres() != null && !composant.getMainOeuvres().isEmpty()) {
                        System.out.println("****** Affichage de Main-d'œuvre : ******");
                        for (MainOeuvre mainOeuvre : composant.getMainOeuvres()) {
                            double coutMainOeuvre = mainOeuvre.getTauxHoraire() * mainOeuvre.getHeuresTravail();
                            totalCoutMainOeuvre += coutMainOeuvre;
                            System.out.printf("- %s : %.2f € (taux horaire : %.2f €/heure, heures de travail : %.2f, productivité : %.1f)\n",
                                    composant.getNom(),
                                    coutMainOeuvre,
                                    mainOeuvre.getTauxHoraire(),
                                    mainOeuvre.getHeuresTravail(),
                                    mainOeuvre.getProductiviteOuvrier());
                        }
                        System.out.printf("**Coût total de la main-d'œuvre : %.2f €**\n", totalCoutMainOeuvre);
                    }

                });
            } else {
                System.out.println("Aucun composant trouvé pour ce projet.");
            }

        });
    }
}
