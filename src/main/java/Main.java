import bean.Composant;
import config.ConnectionConfig;
import service.*;
import ui.*;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        ProjetService projetService=new ProjetService();
        ProjetMenu projetMenu=new ProjetMenu(projetService);
        DevisService devisService=new DevisService();
        DevisMenu devisMenu=new DevisMenu(devisService);
        ClientService clientService=new ClientService();
        ClientMenu clientMenu=new ClientMenu(clientService);
//        MateriauService materiauService=new MateriauService();
//        MateriauMenu materiauMenu=new MateriauMenu(materiauService);

        PrincipalMenu menu=new PrincipalMenu(devisMenu,projetMenu,clientMenu);
        menu.principalMenu();
//

//        ComposantService composantService = new ComposantService();
//        ComposantMenu composantMenu=new ComposantMenu(composantService);
//        composantMenu.composantMenu();

    }
}