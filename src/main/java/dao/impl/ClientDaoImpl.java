package dao.impl;

import bean.Client;
import config.ConnectionConfig;
import dao.dao.ClientDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {

    @Override
    public Client save(Client client) {
        String sql = "INSERT INTO Clients (nom, adresse, telephone, estProfessionnel) VALUES (?, ?, ?, ?) RETURNING id;";
        try (Connection conn = ConnectionConfig.getInstance().getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getNom());
            preparedStatement.setString(2, client.getAdresse());
            preparedStatement.setString(3, client.getTelephone());
            preparedStatement.setBoolean(4, client.isEstProfessionnel());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du client : " + e.getMessage());
        }
        return client;
    }

    @Override
    public Optional<Client> findById(int clientId) {
        String query = "SELECT * FROM clients WHERE id = ?";
        try (Connection conn = ConnectionConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setAdresse(rs.getString("adresse"));
                client.setTelephone(rs.getString("telephone"));
                client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));

                return Optional.of(client);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du client", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients";

        try (Connection conn = ConnectionConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                String telephone = rs.getString("telephone");
                boolean estProfessionnel = rs.getBoolean("estProfessionnel");

                clients.add(new Client(id,nom,adresse,telephone,estProfessionnel));
            }
        } catch (SQLException sqlException) {
            System.out.println("Error fetching clients: " + sqlException.getMessage());
        }

        return clients;
    }

    @Override
    public Client update(Client client) {
        String query = "UPDATE clients SET nom = ?, adresse = ?, telephone = ?, estProfessionnel = ? WHERE id = ?";

        try (Connection conn = ConnectionConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getAdresse());
            pstmt.setString(3, client.getTelephone());
            pstmt.setBoolean(4, client.isEstProfessionnel());
            pstmt.setInt(5, client.getId());
            pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Error updating client: " + sqlException.getMessage());
        }
        return client;
    }

    @Override
    public void delete(int clientId) {
        String query = "DELETE FROM clients WHERE id = ?";

        try (Connection conn = ConnectionConfig.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            pstmt.executeUpdate();
            System.out.println("Client deleted successfully!");
        } catch (SQLException sqlException) {
            System.out.println("Error deleting client: " + sqlException.getMessage());
        }

    }

    @Override
    public List<Client> findByName(String name) {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients WHERE nom = ?";
        try (Connection conn = ConnectionConfig.getInstance().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                String telephone = rs.getString("telephone");
                boolean estProfessionnel = rs.getBoolean("estProfessionnel");
                clients.add(new Client(id,nom,adresse,telephone,estProfessionnel));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }
}
