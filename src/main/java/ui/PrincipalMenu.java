package ui;

import bean.Devis;

import java.util.List;
import java.util.Scanner;

public class PrincipalMenu {
    private DevisMenu devisMenu;
    private ProjetMenu projetMenu;
    private Scanner scanner;
    public PrincipalMenu(DevisMenu devisMenu, ProjetMenu projetMenu){
        this.devisMenu = devisMenu;
        this.projetMenu = projetMenu;
        this.scanner=new Scanner(System.in);

    }
    public void PrincipalMenu() {
        while (true) {
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets");
            System.out.println("3. Calculer le cout total d'un projet'");
            System.out.println("4. Gestion de Devis ");
            System.out.println("5. Quitter");
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

                    break;
                case 2:
                    break;
                case 3:

                    break;
                case 4:devisMenu.devisMenu();
                    break;

                case 5:

                    break;
                case 6:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }

}
