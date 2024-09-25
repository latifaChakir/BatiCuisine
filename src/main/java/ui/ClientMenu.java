package ui;

import bean.Client;
import exceptions.ClientValidationException;
import service.ClientService;
import utils.Validations;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ClientMenu {
    private ClientService clientService;
    private Scanner scanner;

    public ClientMenu(ClientService clientService, Scanner scanner) {
        this.clientService = clientService;
        this.scanner = scanner;
    }

    public void clientMenu() {
        while (true) {
            System.out.println("1. ➤ Ajouter Client");
            System.out.println("2. ➤ Modifier Client");
            System.out.println("3. ➤ Supprimer Client par id");
            System.out.println("4. ➤ Chercher Client par id");
            System.out.println("5. ➤ Chercher Client par nom");
            System.out.println("6. ➤ Afficher tous les clients");
            System.out.println("7. ➤ Quitter");
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
                    addNewClient();
                    System.out.println("Client ajouté avec succès.");
                    break;
                case 2:
                    updateClient();
                    break;
                case 3:
                    int clientIdToDelete = getClientIdInput();
                    clientService.delete(clientIdToDelete);
                    System.out.println("Client supprimé avec succès.");
                    break;
                case 4:
                    int clientId = getClientIdInput();
                    Client clientFound = clientService.findById(clientId);
                    if (clientFound != null) {
                        System.out.println("Client trouvé ⚙ :");
                        System.out.println("✨ ID de client: " + clientFound.getId());
                        System.out.println("✨ Nom: " + clientFound.getNom());
                        System.out.println("✨ Adresse: " + clientFound.getAdresse());
                        System.out.println("✨ Téléphone: " + clientFound.getTelephone());
                        System.out.println("✨ est professionnel : " + clientFound.isEstProfessionnel());

                    } else {
                        System.out.println("Client non trouvé.");
                    }
                    break;
                case 5:
                    String clientName = getClientNameInput();
                    findClientByName(clientName);
                    break;
                case 6:
                    List<Client> clients = clientService.findAll();
                    if (!clients.isEmpty()) {
                        for (Client c : clients) {
                            System.out.println("------------------------------------------------------");
                            System.out.println("✦ ID de client: " + c.getId());
                            System.out.println("✦ Nom: " + c.getNom());
                            System.out.println("✦ Adresse: " + c.getAdresse());
                            System.out.println("✦ Téléphone: " + c.getTelephone());
                            System.out.println("✦ est professionnel : " + c.isEstProfessionnel());
                            System.out.println("------------------------------------------------------");

                        }
                    } else {
                        System.out.println("Aucun client trouvé.");
                    }
                    break;
                case 7:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }

    private Client getClientInput() {
        System.out.print("Entrer le nom du client: ");
        String nom = scanner.nextLine();
        System.out.print("Entrer l'adresse du client: ");
        String adresse = scanner.nextLine();
        System.out.print("Entrer le téléphone du client: ");
        String telephone = scanner.nextLine();
        System.out.print("Le client est professionnel (true/false) ?");
        boolean estProfessionnel = scanner.nextBoolean();
        scanner.nextLine();
        Client client = new Client(nom, adresse, telephone, estProfessionnel);
        try {
            Validations.clientValidation(client);
        } catch (ClientValidationException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return client;
    }


    private int getClientIdInput() {
        System.out.print("Entrer l'ID du client: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private String getClientNameInput() {
        System.out.print("Entrer le nom du client: ");
        return scanner.nextLine();
    }

        public List<Client> findClientByName(String clientName) {
        List<Client> clientsByName = clientService.findByNom(clientName);
        if (!clientsByName.isEmpty()) {
            clientsByName.stream()
                    .sorted(Comparator.comparing(Client::getNom))
                    .forEach(c->{
                        System.out.println("Client trouvé ���");
                        System.out.println();
                        System.out.println("✦ ID de client: " + c.getId());
                        System.out.println("✦ Nom: " + c.getNom());
                        System.out.println("✦ Adresse: " + c.getAdresse());
                        System.out.println("✦ Téléphone: " + c.getTelephone());
                        System.out.println("✦ est professionnel : " + c.isEstProfessionnel());
                        System.out.println();
                    }
                    );
        } else {
            System.out.println("Aucun client trouvé avec ce nom.");
        }
            return clientsByName;
        }

        public Client addNewClient() {
            Client client = getClientInput();
            if (client != null) {
                clientService.save(client);
            } else {
                System.err.println("Le client n'a pas été ajouté en raison d'erreurs de validation.");
            }
            return client;
        }

    public void updateClient() {
        System.out.print("Entrer l'ID du client à modifier: ");
        int clientId = Integer.parseInt(scanner.nextLine());
        Client client = clientService.findById(clientId);

        if (client != null) {
            System.out.print("Entrer un nouveau nom (actuel: " + client.getNom() + "): ");
            String newName = scanner.nextLine();
            if (!newName.trim().isEmpty()) {
                client.setNom(newName);
            }

            System.out.print("Entrer une nouvelle adresse (actuelle: " + client.getAdresse() + "): ");
            String newAdresse = scanner.nextLine();
            if (!newAdresse.trim().isEmpty()) {
                client.setAdresse(newAdresse);
            }

            System.out.print("Entrer un nouveau téléphone (actuel: " + client.getTelephone() + "): ");
            String newTelephone = scanner.nextLine();
            if (!newTelephone.trim().isEmpty()) {
                client.setTelephone(newTelephone);
            }

            System.out.print("Le client est professionnel (actuel: " + client.isEstProfessionnel() + ") (true/false) ? ");
            String isProInput = scanner.nextLine();
            if (!isProInput.trim().isEmpty()) {
                client.setEstProfessionnel(Boolean.parseBoolean(isProInput));
            }
            try {
                Validations.clientValidation(client);
                clientService.update(client);
                System.out.println("Client modifié avec succès.");
            } catch (ClientValidationException e) {
                System.out.println("Erreur de validation: " + e.getMessage());
            }

        } else {
            System.out.println("Client non trouvé.");
        }
    }

}
