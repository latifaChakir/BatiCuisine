package ui;

import bean.Devis;
import bean.Projet;
import service.DevisService;
import service.ProjetService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DevisMenu {
    private DevisService devisService;
    private ProjetService projetService;
    private Scanner scanner;
    public DevisMenu(DevisService devisService) {
        this.devisService = devisService;
        this.projetService = new ProjetService();
        this.scanner = new Scanner(System.in);
    }
    public void devisMenu() {
        while (true) {
            System.out.println("1. Ajouter Devis");
            System.out.println("2. Modifier Devis");
            System.out.println("3. Supprimer Devis par id");
            System.out.println("4. Chercher Devis par id");
            System.out.println("5. Afficher tous les Deviss");
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
                     Devis devis=getDevisInput();
                    if (devis != null) {
                        devisService.save(devis);
                    } else {
                        System.err.println("Le devis n'a pas été ajouté en raison d'erreurs de validation.");
                    }
                    break;
                case 2:
                    long devisIdToUpdate = getDevisIdInput();
                    Devis devisToUpdate = getDevisInput();
                    devisToUpdate.setId(devisIdToUpdate);
                    System.out.print("le devis est accepté? OUI/NON");
                    String reponse=scanner.nextLine();
                    if(reponse.equals("OUI")){
                        devisToUpdate.setAccepted(true);
                    }
                    devisService.update(devisToUpdate);
                    System.out.println("Devis modifié avec succès.");

                    break;
                case 3:
                    int DevisIdToDelete = getDevisIdInput();
                    devisService.delete(DevisIdToDelete);
                    System.out.println("Devis supprimé avec succès.");
                    break;
                case 4:
                    int DevisId = getDevisIdInput();
                    Devis DevisFound = devisService.findById(DevisId);
                    if (DevisFound != null) {
                        System.out.println(DevisFound);
                    } else {
                        System.out.println("Devis non trouvé.");
                    }
                    break;

                case 5:
                    List<Devis> Deviss = devisService.findAll();
                    if (!Deviss.isEmpty()) {
                        for (Devis c : Deviss) {
                            System.out.println(c);
                        }
                    } else {
                        System.out.println("Aucun Devis trouvé.");
                    }
                    break;
                case 6:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }

    private int getDevisIdInput() {
        System.out.println("Entrer l'ID du devis: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private Devis getDevisInput(){
        System.out.print("Entrer l'ID du projet: ");
        int projetId=scanner.nextInt();
        Projet projet=new Projet();
        projet.setId(projetId);
        scanner.nextLine();
        Optional<Projet> projet1=projetService.findById(projetId);
        double montantEstimation=projet1.get().getCoutTotal();
        System.out.print("Entrer la date d'emission: ");
        LocalDate estimationDate=LocalDate.parse(scanner.nextLine());
        System.out.print("Entrer la date de validation: ");
        LocalDate dateValidation=LocalDate.parse(scanner.nextLine());
        Devis devis=new Devis(0L, montantEstimation,estimationDate,dateValidation,false,projet);
        return devis;
    }



}
