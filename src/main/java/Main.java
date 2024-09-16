import config.ConnectionConfig;
import service.ClientService;
import ui.ClientMenu;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {

        Connection connection = ConnectionConfig.getInstance().getConnection();
        ClientService clientService=new ClientService();
        ClientMenu clientMenu=new ClientMenu(clientService);
        clientMenu.clientMenu();

    }
}