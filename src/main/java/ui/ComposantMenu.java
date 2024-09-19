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
//                    ajouterComposantAuProjet();
                    break;
                case 2:
//                    modifierComposant();
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

    public void ajouterComposantAuProjet(Projet projet) {
        boolean ajouterAutreComposant = true;
        while (ajouterAutreComposant) {
            System.out.println("Ajouter un composant au projet: " + projet.getNomProjet());

            // Appel pour obtenir l'entrée du composant et sauvegarder
            getComposantInput(projet);

            System.out.print("Voulez-vous ajouter un autre composant ? (oui/non): ");
            String reponse = scanner.nextLine();

            if (!reponse.equalsIgnoreCase("oui")) {
                ajouterAutreComposant = false;
            }
        }
    }

//    private void modifierComposant() {
//        System.out.println("Modifier un composant");
//        int composantIdToUpdate = getComposantIdInput();
//        Composant composantToUpdate = getComposantInput();
//        composantToUpdate.setId(composantIdToUpdate);
//        composantService.update(composantToUpdate);
//        System.out.println("Composant modifié avec succès.");
//    }

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

    private void getComposantInput(Projet projet) {
        System.out.print("Type de composant (1: Materiel, 2: MainDOeuvre): ");
        int type = Integer.parseInt(scanner.nextLine());

        if (type == 1) {
            boolean ajouterAutreMateriau = true;

            while (ajouterAutreMateriau) {
                Composant materiau = getMateriauInput(projet);

                // Sauvegarde chaque matériau ajouté
                composantService.save(materiau);
                System.out.println("Matériau ajouté avec succès !");

                System.out.print("Voulez-vous ajouter un autre matériau ? (y/n): ");
                String reponse = scanner.nextLine();

                if (!reponse.equalsIgnoreCase("y")) {
                    ajouterAutreMateriau = false;
                }
            }

        } else {
            boolean ajouterAutreMainOeuvre = true;

            while (ajouterAutreMainOeuvre) {
                Composant mainOeuvre = getMainOeuvreInput(projet);

                // Sauvegarde chaque main-d'œuvre ajoutée
                composantService.save(mainOeuvre);
                System.out.println("Main d'œuvre ajoutée avec succès !");

                System.out.print("Voulez-vous ajouter une autre main d'œuvre ? (y/n): ");
                String reponse = scanner.nextLine();

                if (!reponse.equalsIgnoreCase("y")) {
                    ajouterAutreMainOeuvre = false;
                }
            }
        }
    }

    private Composant getMateriauInput(Projet projet) {
        System.out.print("Entrer le nom du matériau: ");
        String nom = scanner.nextLine();
        System.out.print("Entrer le taux de TVA: ");
        double tauxTVA = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le coût unitaire: ");
        double coutUnitaire = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer la quantité: ");
        double quantite = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le coût de transport: ");
        double coutTransport = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le coefficient de qualité: ");
        double coefficientQualite = Double.parseDouble(scanner.nextLine());

        // Ne sauvegardez pas ici, laissez `ajouterComposantAuProjet` gérer la sauvegarde
        return new Materiau(0, nom, TypeComposant.Materiel, tauxTVA, coutUnitaire, quantite, coutTransport, coefficientQualite, projet);
    }

    private Composant getMainOeuvreInput(Projet projet) {
        System.out.print("Entrer le nom de l'ouvrier: ");
        String nom = scanner.nextLine();
        System.out.print("Entrer le taux de TVA: ");
        double tauxTVA = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le taux horaire: ");
        double tauxHoraire = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le nombre d'heures travaillées: ");
        double heuresTravail = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer la productivité de l'ouvrier: ");
        double productiviteOuvrier = Double.parseDouble(scanner.nextLine());

        return new MainOeuvre(0, nom, TypeComposant.MainDOeuvre, projet, tauxTVA, tauxHoraire, heuresTravail, productiviteOuvrier);
    }

    private int getComposantIdInput() {
        System.out.print("Entrer l'ID du composant: ");
        return Integer.parseInt(scanner.nextLine());
    }


}
