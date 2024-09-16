package ui;

import bean.Composant;
import bean.Materiau;
import bean.MainOeuvre;
import bean.Projet;
import bean.enums.TypeComposant;
import service.ComposantService;

import java.util.List;
import java.util.Scanner;

public class ComposantMenu {
    private ComposantService composantService;
    private Scanner scanner;

    public ComposantMenu(ComposantService composantService) {
        this.composantService = composantService;
        this.scanner = new Scanner(System.in);
    }

    public void composantMenu() {
        while (true) {
            System.out.println("1. Ajouter Composant");
            System.out.println("2. Modifier Composant");
            System.out.println("3. Supprimer Composant par id");
            System.out.println("4. Chercher Composant par id");
            System.out.println("5. Afficher tous les composants");
            System.out.println("6. Quitter");
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
                    ajouterComposant();
                    break;
                case 2:
                    modifierComposant();
                    break;
                case 3:
                    supprimerComposant();
                    break;
                case 4:
                    chercherComposantParId();
                    break;
                case 5:
                    afficherTousLesComposants();
                    break;
                case 6:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }

    private void ajouterComposant() {
        System.out.println("Ajouter un composant");
        Composant composant = getComposantInput();
        composantService.save(composant);
        System.out.println("Composant ajouté avec succès.");
    }

    private void modifierComposant() {
        System.out.println("Modifier un composant");
        int composantIdToUpdate = getComposantIdInput();
        Composant composantToUpdate = getComposantInput();
        composantToUpdate.setId(composantIdToUpdate);
        composantService.update(composantToUpdate);
        System.out.println("Composant modifié avec succès.");
    }

    private void supprimerComposant() {
        int composantIdToDelete = getComposantIdInput();
        composantService.delete(composantIdToDelete);
        System.out.println("Composant supprimé avec succès.");
    }

    private void chercherComposantParId() {
        int composantId = getComposantIdInput();
        Composant composantFound = composantService.findById(composantId);
        if (composantFound != null) {
            System.out.println(composantFound);
        } else {
            System.out.println("Composant non trouvé.");
        }
    }

    private void afficherTousLesComposants() {
        List<Composant> composants = composantService.findAll();
        if (!composants.isEmpty()) {
            for (Composant c : composants) {
                System.out.println(c);
            }
        } else {
            System.out.println("Aucun composant trouvé.");
        }
    }

    private Composant getComposantInput() {
        System.out.println("Type de composant (1: Materiel, 2: MainDOeuvre): ");
        int type = Integer.parseInt(scanner.nextLine());

        System.out.println("Entrer le nom du composant: ");
        String nom = scanner.nextLine();
        System.out.println("Entrer le taux de TVA: ");
        double tauxTVA = Double.parseDouble(scanner.nextLine());
        System.out.println("Entrer l'ID du projet: ");
        int projetId = Integer.parseInt(scanner.nextLine());
        Projet projet=new Projet();
        projet.setId(projetId);

        if (type == 1) {
            System.out.println("Entrer le coût unitaire: ");
            double coutUnitaire = Double.parseDouble(scanner.nextLine());
            System.out.println("Entrer la quantité: ");
            double quantite = Double.parseDouble(scanner.nextLine());
            System.out.println("Entrer le coût de transport: ");
            double coutTransport = Double.parseDouble(scanner.nextLine());
            System.out.println("Entrer le coefficient de qualité: ");
            double coefficientQualite = Double.parseDouble(scanner.nextLine());

            TypeComposant typeComposant = TypeComposant.Materiel;

            return new Materiau(0, nom, typeComposant, tauxTVA, coutUnitaire, quantite, coutTransport, coefficientQualite,projet);

        } else {
            System.out.println("Entrer le taux horaire: ");
            double tauxHoraire = Double.parseDouble(scanner.nextLine());
            System.out.println("Entrer le nombre d'heures travaillées: ");
            double heuresTravail = Double.parseDouble(scanner.nextLine());
            System.out.println("Entrer la productivité de l'ouvrier: ");
            double productiviteOuvrier = Double.parseDouble(scanner.nextLine());

            TypeComposant typeComposant = TypeComposant.MainDOeuvre;
            return new MainOeuvre(0, nom, typeComposant, projet, tauxTVA, tauxHoraire, heuresTravail, productiviteOuvrier);
        }
    }

    private int getComposantIdInput() {
        System.out.println("Entrer l'ID du composant: ");
        return Integer.parseInt(scanner.nextLine());
    }
}
