package ui;

import bean.Client;
import bean.Projet;
import bean.enums.EtatProjet;
import service.ComposantService;
import service.ProjetService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjetMenu {
    private ProjetService projetService;
    private static Scanner scanner;

    public ProjetMenu(ProjetService projetService) {
        this.projetService = projetService;
        scanner = new Scanner(System.in);
    }

    public void projetMenu() {
        while (true) {
            System.out.println("1. Ajouter Projet");
            System.out.println("2. Modifier Projet");
            System.out.println("3. Supprimer Projet par id");
            System.out.println("4. Chercher Projet par id");
            System.out.println("5. Chercher Projet by nom");
            System.out.println("6. Afficher les Projets ");
            System.out.print("choisis une option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    Projet projet = inputsProjet();
                    projetService.saveProjetClient(projet, projet.getClient());
                    System.out.println("Voulez-vous ajouter des composants à ce projet ? (oui/non)");
                    String reponse = scanner.nextLine();
                    if (reponse.equalsIgnoreCase("oui")) {
                        ComposantMenu composantMenu = new ComposantMenu(new ComposantService());
                        composantMenu.ajouterComposantAuProjet(projet);
                    }
                    break;
                case 2:
                    int projetIdToUpdate=getProjetIdInput();
                    Projet projetToUpdate = inputsProjet();
                    projetToUpdate.setId(projetIdToUpdate);
                    projetService.updateProjetClient(projetToUpdate,projetToUpdate.getClient());
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
                    System.out.println("Bye!");
                    return; // Exit the loop
                default:
                    System.out.println("Option non valide");
            }
        }
    }

    private static Client clientInput() {
        System.out.println("Entrer le nom du client: ");
        String nom = scanner.nextLine();
        System.out.println("Entrer l'adresse du Client: ");
        String adresse = scanner.nextLine();
        System.out.println("Entrer le telephone du Client: ");
        String telephone = scanner.nextLine();
        System.out.println("Le client est professionnel(true/false)?");
        boolean estProfessionnel = Boolean.parseBoolean(scanner.nextLine());
        return new Client(0, nom, adresse, telephone, estProfessionnel);
    }

    private static Projet inputsProjet() {
        Client client = clientInput();
        System.out.print("Nom du projet : ");
        String nomProjet = scanner.nextLine();
        System.out.print("Marge beneficiaire: ");
        double margeBenif = Double.parseDouble(scanner.nextLine());

        System.out.print("Cout total : ");
        double coutTotal = Double.parseDouble(scanner.nextLine());

        System.out.print("Etat du projet (par ex: ENCOURS, TERMINE, ANNULE) : ");
        String etatInput = scanner.nextLine().toUpperCase();
        EtatProjet etat = EtatProjet.valueOf(etatInput);
        return new Projet(nomProjet, margeBenif, coutTotal, etat, client);
    }

    private void deleteProjetById() {
        System.out.println("Enter the ID of the project to delete: ");
        int projectId = Integer.parseInt(scanner.nextLine());
        projetService.delete(projectId);
        System.out.println("Projet supprimé.");
    }

    private void findProjetById() {
        System.out.println("Enter the ID of the project: ");
        int projectId = Integer.parseInt(scanner.nextLine());
        Optional<Projet> projet = projetService.findById(projectId);
        if (projet.isPresent()) {
            System.out.println(projet.get());
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

    private void findProjetByName() {
        System.out.println("Enter the name of the project: ");
        String projetName = scanner.nextLine();
        List<Projet> projets = projetService.findByName(projetName);
        projets.forEach(System.out::println);
    }

    private int getProjetIdInput() {
        System.out.println("Entrer l'ID du projet: ");
        return Integer.parseInt(scanner.nextLine());
    }

}
