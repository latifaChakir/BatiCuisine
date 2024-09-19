import bean.Composant;
import config.ConnectionConfig;
import service.ClientService;
import service.ComposantService;
import service.DevisService;
import service.ProjetService;
import ui.ClientMenu;
import ui.ComposantMenu;
import ui.DevisMenu;
import ui.ProjetMenu;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

//        Connection connection = ConnectionConfig.getInstance().getConnection();
        ProjetService projetService=new ProjetService();
        ProjetMenu projetMenu=new ProjetMenu(projetService);
        projetMenu.projetMenu();
//
//        ClientService clientService=new ClientService();
//        ClientMenu clientMenu=new ClientMenu(clientService);
//        clientMenu.clientMenu();
//        ComposantService composantService = new ComposantService();
//        ComposantMenu composantMenu=new ComposantMenu(composantService);
//        composantMenu.composantMenu();

//        DevisService devisService=new DevisService();
//        DevisMenu devisMenu=new DevisMenu(devisService);
//        devisMenu.devisMenu();

    }
}