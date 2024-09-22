package ui;
import java.util.Scanner;

public class PrincipalMenu {
    private DevisMenu devisMenu;
    private ProjetMenu projetMenu;
    private ClientMenu clientMenu;
    private ComposantMenu composantMenu;
    private Scanner scanner;

    public PrincipalMenu(DevisMenu devisMenu, ProjetMenu projetMenu, ClientMenu clientMenu,Scanner scanner,ComposantMenu composantMenu) {
        this.devisMenu = devisMenu;
        this.projetMenu = projetMenu;
        this.clientMenu = clientMenu;
        this.composantMenu = composantMenu;
        this.scanner = new Scanner(System.in);
    }
    public void principalMenu() {
        while (true) {
            System.out.println("╔═════════════════════════════════════════╗");
            System.out.println("║             BATI-CUISINE                ║");
            System.out.println("╚═════════════════════════════════════════╝");
            System.out.println("║ 1. ➕ Créer un nouveau projet");
            System.out.println("║ 2. \uD83D\uDD04 update projets");
            System.out.println("║ 3. \uD83D\uDDA5\uFE0F Afficher tous les projets");
            System.out.println("║ 4. \uD83D\uDCB0 Calculer le cout total d'un projet ");
            System.out.println("║ 5. \uD83D\uDD0D Afficher un projet par son ID ");
            System.out.println("║ 6. \uD83D\uDDD1\uFE0F Supprimer le projet ");
            System.out.println("║ 7. \uD83D\uDCC4 Gestion de Devis ");
            System.out.println("║ 8. \uD83D\uDC64 Gestion de Clients ");
            System.out.println("║ 9. ❌ Quitter");
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
                case 4:composantMenu.calculTotalProjet();
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
                    break;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }


}
