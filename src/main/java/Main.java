import service.*;
import ui.*;

import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        DevisService devisService = new DevisService();
        ProjetService projetService = new ProjetService();
        ComposantService composantService = new ComposantService();
        ClientService clientService = new ClientService();

        ClientMenu clientMenu = new ClientMenu(clientService, scanner);
        DevisMenu devisMenu = new DevisMenu(devisService, scanner);
        ProjetMenu projetMenu = new ProjetMenu(projetService, devisService, composantService, clientMenu, scanner);
        ComposantMenu composantMenu = new ComposantMenu(composantService, scanner);

        PrincipalMenu principalMenu = new PrincipalMenu(devisMenu, projetMenu, clientMenu, scanner,composantMenu);
        principalMenu.principalMenu();

        scanner.close();


    }
}