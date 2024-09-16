package ui;

import bean.Client;
import bean.Projet;
import bean.enums.EtatProjet;
import service.ProjetService;

import java.util.List;
import java.util.Scanner;

public class ProjetMenu {
    private ProjetService projetService;
    private static Scanner scanner;
    public ProjetMenu() {
        projetService = new ProjetService();
        scanner = new Scanner(System.in);
    }
    public void projetMenu()  {
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
                    Projet projet=inputsProjet();
                    projetService.saveProjetClient(projet,projet.getClient());
                    break;
                case 2:
                    Projet projet2=inputsProjet();
                    projetService.update(projet2);
                    break;
                case 3:
                    projetService.delete();
                    break;
                case 4:
                    break;
                case 5:
                    List<Projet> Projets1=projetService.findByName();
                    for(Projet Projet2:Projets1){
                        System.out.println(Projet2);
                    }
                    break;
                case 6:
                    List<Projet> Projets = projetService.findAll();
                    System.out.println(Projets);
                    break;

                case 7:
                    System.out.println("Bye!");
                    break;
            }
        }
    }
    private static Client clientInput(){
        System.out.println("Entrer le nom du client: ");
        String nom = scanner.nextLine();
        System.out.println("Entrer l'adresse du Client: ");
        String adresse = scanner.nextLine();
        System.out.println("Entrer le telephone du Client: ");
        String telephone = scanner.nextLine();
        System.out.println("Le client est professionnel(true/false)?");
        boolean estProfessionnel = scanner.nextBoolean();
       return new Client(nom, adresse, telephone, estProfessionnel);
    }
    private static Projet inputsProjet() {
        Client client = clientInput();
        System.out.print("Nom du projet : ");
        String nomProjet = scanner.nextLine();
        System.out.print("Marge beneficiaire: ");
        double margeBenif = Double.parseDouble(scanner.nextLine());
        System.out.print("cout total : ");
        double coutTotal = Double.parseDouble(scanner.nextLine());
        System.out.print("Etat du projet : ");
        String etatInput = scanner.nextLine().toUpperCase();
        EtatProjet etat =EtatProjet.valueOf(etatInput);
        return new Projet(nomProjet,margeBenif,coutTotal,etat,client);
    }



}
