package ui;

import bean.Client;
import bean.Projet;
import bean.enums.EtatProjet;
import service.ClientService;
import service.ComposantService;
import service.ProjetService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjetMenu {
    private ProjetService projetService;
    private ClientMenu clientMenu;
    private static Scanner scanner;

    public ProjetMenu(ProjetService projetService) {
        this.projetService = projetService;
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

    private void findProjetByName() {
        System.out.print("Entrer le nom du projet : ");
        String projetName = scanner.nextLine();
        List<Projet> projets = projetService.findByName(projetName);
        if (!projets.isEmpty()) {
            projets.forEach(System.out::println);
        } else {
            System.out.println("Aucun projet trouvé avec ce nom.");
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
    public void updateProject() {
        System.out.print("Entrer l'ID du projet à mettre à jour : ");
        int projectId = Integer.parseInt(scanner.nextLine());
        Optional<Projet> projet = projetService.findById(projectId);
        if (projet.isPresent()) {
            System.out.println("Projet trouvé : " + projet.get());
            System.out.println("Entrer les nouvelles informations du projet : ");
            Projet updatedProjet = inputsProjet();
            updatedProjet.setId(projectId);
            projetService.update(updatedProjet);
            System.out.println("Projet mis à jour avec succès.");
            System.out.println("Voulez-vous ajouter des composants à ce projet ? (oui/non)");
            String reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("oui")) {
                ComposantMenu composantMenu = new ComposantMenu(new ComposantService());
//                composantMenu.updateComposantduProjet(updatedProjet);
            }
        } else {
            System.out.println("Projet non trouvé.");
        }
    }
}
