import config.ConnectionConfig;
import service.ClientService;
import service.ProjetService;
import ui.ClientMenu;
import ui.ProjetMenu;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

        Connection connection = ConnectionConfig.getInstance().getConnection();
        ProjetService projetService=new ProjetService();
        ProjetMenu projetMenu=new ProjetMenu(projetService);
        projetMenu.projetMenu();
//
//        ClientService clientService=new ClientService();
//        ClientMenu clientMenu=new ClientMenu(clientService);
//        clientMenu.clientMenu();

    }
}