package ui;

import bean.Client;
import bean.Projet;
import bean.enums.EtatProjet;
import exceptions.ClientValidationException;
import service.ClientService;
import service.ComposantService;
import service.ProjetService;
import utils.Validations;

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

    public void projetMenu() {
        while (true) {
            System.out.println("1. Ajouter Projet");
            System.out.println("2. Modifier Projet");
            System.out.println("3. Supprimer Projet par id");
            System.out.println("4. Chercher Projet par id");
            System.out.println("5. Chercher Projet par nom");
            System.out.println("6. Afficher les Projets");
            System.out.println("7. Quitter");
            System.out.print("Choisir une option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                continue;
            }

            switch (choice) {
                case 1:
                    Projet projet = inputsProjet();
                    handleSaveProject(projet, projet.getClient());
                    System.out.println("Voulez-vous ajouter des composants à ce projet ? (oui/non)");
                    String reponse = scanner.nextLine();
                    if (reponse.equalsIgnoreCase("oui")) {
                        ComposantMenu composantMenu = new ComposantMenu(new ComposantService());
                        composantMenu.ajouterComposantAuProjet(projet);
                    }
                    break;

                case 2:
                    int projetIdToUpdate = getProjetIdInput();
                    Projet projetToUpdate = inputsProjet();
                    projetToUpdate.setId(projetIdToUpdate);
                    projetService.updateProjetClient(projetToUpdate, projetToUpdate.getClient());
                    break;
                case 3:
                    deleteProjetById();
                    break;
                case 4:
                    findProjetById();
                    break;
                case 5:
                    findProjetByName();
                    break;
                case 6:
                    List<Projet> projets = projetService.findAll();
                    projets.forEach(System.out::println);
                    break;
                case 7:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option non valide.");
            }
        }
    }

    private Client clientInput() {
        Client client = null;
        boolean valid = false;

        while (!valid) {
            System.out.print("Entrer le nom du client: ");
            String nom = scanner.nextLine();
            System.out.print("Entrer l'adresse du Client: ");
            String adresse = scanner.nextLine();
            System.out.print("Entrer le téléphone du Client: ");
            String telephone = scanner.nextLine();
            System.out.print("Le client est professionnel (true/false)? ");
            boolean estProfessionnel = Boolean.parseBoolean(scanner.nextLine());

            client = new Client(0, nom, adresse, telephone, estProfessionnel);
            try {
                Validations.clientValidation(client);
                valid = true;
            } catch (ClientValidationException e) {
                System.err.println(e.getMessage());
                System.out.println("Veuillez entrer les informations du client à nouveau.");
            }
        }
        return client;
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

        System.out.print("Marge bénéficiaire : ");
        double margeBenif = Double.parseDouble(scanner.nextLine());

        System.out.print("Coût total : ");
        double coutTotal = Double.parseDouble(scanner.nextLine());

        System.out.print("État du projet (par ex: ENCOURS, TERMINE, ANNULE) : ");
        String etatInput = scanner.nextLine().toUpperCase();
        EtatProjet etat = EtatProjet.valueOf(etatInput);

        return new Projet(nomProjet, margeBenif, coutTotal, etat, client, surface);
    }

    private Client searchClientForProject() {
        System.out.print("Entrer le nom du client : ");
        String name = scanner.nextLine();
        List<Client> clientsByName = clientMenu.findClientByName(name);

        if (!clientsByName.isEmpty()) {
            System.out.println("Sélectionnez un client par ID ou tapez '0' pour annuler.");

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

    public void addOrSearchClientMenu() {
        while (true) {
            System.out.println("\n--- Gestion des Clients ---");
            System.out.println("1. Chercher un Client existant");
            System.out.println("2. Ajouter un nouveau Client");
            System.out.println("3. Quitter");

            System.out.print("Choisir une option : ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                continue;
            }

            switch (choice) {
                case 1:
                    searchClient();
                    break;
                case 2:
                    addNewClient();
                    break;
                case 3:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option non valide.");
            }
        }
    }

    private void searchClient() {
        System.out.print("Entrer le nom du client : ");
        String name = scanner.nextLine();
        List<Client> clientsByName = clientMenu.findClientByName(name);
        if (!clientsByName.isEmpty()) {
            clientsByName.forEach(System.out::println);
            System.out.println("Voulez-vous ajouter un projet à ce client ? (oui/non)");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("oui")) {
                Client selectedClient = clientsByName.get(0);
                addProjet(selectedClient);
            }
        } else {
            System.out.println("Client non trouvé.");
        }
    }

    private void addNewClient() {
        Client selectedClient = clientMenu.addNewClient();
        if (selectedClient != null) {
            addProjet(selectedClient);
        } else {
            System.out.println("Échec de l'ajout du nouveau client.");
        }
    }

    private void addProjet(Client client) {
        Projet projet = inputsProjet();
        projet.setClient(client);
        projetService.saveProjetClient(projet, client);
        System.out.println("Projet ajouté avec succès.");
    }
}
