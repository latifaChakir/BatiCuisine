package ui;

import bean.Client;
import service.ClientService;

import java.util.List;
import java.util.Scanner;

public class ClientMenu {
    private ClientService clientService;
    private Scanner scanner;

    public ClientMenu(ClientService clientService) {
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }

    public void clientMenu() {
        while (true) {
            System.out.println("1. Ajouter Client");
            System.out.println("2. Modifier Client");
            System.out.println("3. Supprimer Client par id");
            System.out.println("4. Chercher Client par id");
            System.out.println("5. Chercher Client par nom");
            System.out.println("6. Afficher tous les clients");
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
                    Client client = getClientInput();
                    clientService.save(client);
                    System.out.println("Client ajouté avec succès.");
                    break;
                case 2:
                    int clientIdToUpdate = getClientIdInput();
                    Client clientToUpdate = getClientInput();
                    clientToUpdate.setId(clientIdToUpdate);
                    clientService.update(clientToUpdate);
                    System.out.println("Client modifié avec succès.");
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
                        System.out.println(clientFound);
                    } else {
                        System.out.println("Client non trouvé.");
                    }
                    break;
                case 5:
                    String clientName = getClientNameInput();
                    List<Client> clientsByName = clientService.findByNom(clientName);
                    if (!clientsByName.isEmpty()) {
                        for (Client c : clientsByName) {
                            System.out.println(c);
                        }
                    } else {
                        System.out.println("Aucun client trouvé avec ce nom.");
                    }
                    break;
                case 6:
                    List<Client> clients = clientService.findAll();
                    if (!clients.isEmpty()) {
                        for (Client c : clients) {
                            System.out.println(c);
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
        System.out.println("Entrer le nom du client: ");
        String nom = scanner.nextLine();
        System.out.println("Entrer l'adresse du client: ");
        String adresse = scanner.nextLine();
        System.out.println("Entrer le téléphone du client: ");
        String telephone = scanner.nextLine();
        System.out.println("Le client est professionnel (true/false) ?");
        boolean estProfessionnel = scanner.nextBoolean();
        scanner.nextLine();  // Pour consommer la nouvelle ligne restante
        return new Client(nom, adresse, telephone, estProfessionnel);
    }

    private int getClientIdInput() {
        System.out.println("Entrer l'ID du client: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private String getClientNameInput() {
        System.out.println("Entrer le nom du client: ");
        return scanner.nextLine();
    }
}
