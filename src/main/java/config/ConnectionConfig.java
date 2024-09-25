package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionConfig {

    private static ConnectionConfig instance;
    private String databaseUrl;
    private String user;
    private String password;

    private ConnectionConfig() {
        this.databaseUrl = System.getenv("DB_URL");
        this.user = System.getenv("USER");
        this.password = System.getenv("PASSWORD");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL JDBC non trouvé : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static ConnectionConfig getInstance() {
        if (instance == null) {
            instance = new ConnectionConfig();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(databaseUrl, user, password);
            return conn;
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}
