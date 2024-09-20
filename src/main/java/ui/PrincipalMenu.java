package ui;

import java.util.Scanner;

public class PrincipalMenu {
    private DevisMenu devisMenu;
    private ProjetMenu projetMenu;
    private MateriauMenu materiauMenu;
    private Scanner scanner;
    public PrincipalMenu(DevisMenu devisMenu, ProjetMenu projetMenu){
        this.devisMenu = devisMenu;
        this.projetMenu = projetMenu;
        this.scanner=new Scanner(System.in);

    }
    public void principalMenu() {
        while (true) {
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. update projets");
            System.out.println("3. Afficher tous les projets");
            System.out.println("4. Calculer le cout total d'un projet'");
            System.out.println("5. Gestion de MATERIAUX ");
            System.out.println("6. Gestion de Devis ");
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
                case 1:projetMenu.createProject();
                    break;
                case 2:projetMenu.updateProject();
                    break;
                case 3:projetMenu.findAllProject();
                    break;
                case 4:projetMenu.calculTotalProjet();
                case 5:
                    break;

                case 6:devisMenu.devisMenu();
                    break;
                case 7:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }


}
