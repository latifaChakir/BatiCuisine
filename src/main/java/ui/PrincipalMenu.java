package ui;

import java.util.Scanner;

public class PrincipalMenu {
    private DevisMenu devisMenu;
    private ProjetMenu projetMenu;
    private MateriauMenu materiauMenu;
    private ClientMenu clientMenu;
    private Scanner scanner;
    public PrincipalMenu(DevisMenu devisMenu, ProjetMenu projetMenu,ClientMenu clientMenu){
        this.devisMenu = devisMenu;
        this.projetMenu = projetMenu;
        this.clientMenu=clientMenu;
        this.scanner=new Scanner(System.in);

    }
    public void principalMenu() {
        while (true) {
            System.out.println("╔═════════════════════════════════════════╗");
            System.out.println("║             BATI-CUISINE                ║");
            System.out.println("╚═════════════════════════════════════════╝");
            System.out.println(" 1. Créer un nouveau projet");
            System.out.println(" 2. update projets");
            System.out.println(" 3. Afficher tous les projets");
            System.out.println(" 4. Calculer le cout total d'un projet ");
            System.out.println(" 5. Afficher un projet par son ID ");
            System.out.println(" 6. Supprimer le projet ");
            System.out.println(" 7. Gestion de Devis ");
            System.out.println(" 8. Gestion de Clients ");
            System.out.println(" 9. Quitter");
            System.out.println("╚═════════════════════════════════════════╝");
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
                    break;
                case 5:projetMenu.findProjetById();
                    break;
                case 6:projetMenu.deleteProjetById();
                    break;
                case 7:devisMenu.devisMenu();
                    break;
                case 8:
                    clientMenu.clientMenu();
                    break;
                case 9:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }


}
