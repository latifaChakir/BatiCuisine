package dao.impl;

import bean.Client;
import bean.Projet;
import bean.enums.EtatProjet;
import config.ConnectionConfig;
import dao.dao.ProjetDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjetDaoImpl implements ProjetDao {
    private Connection conn;

    public ProjetDaoImpl(){
        try {
            this.conn = ConnectionConfig.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Projet save(Projet projet) {
        String sql = "INSERT INTO Projets (nomprojet, margebeneficiaire, couttotal, etat, client_id,surface) VALUES (?, ?, ?, ?::EtatProjet, ?,?) RETURNING id;";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, projet.getNomProjet());
            preparedStatement.setDouble(2, projet.getMargeBeneficiaire());
            preparedStatement.setDouble(3, projet.getCoutTotal());
            preparedStatement.setString(4, projet.getEtat().name());
            preparedStatement.setInt(5, projet.getClient().getId());
            preparedStatement.setDouble(6, projet.getSurface());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int generatedId = resultSet.getInt(1);
                System.out.println("ID généré : " + generatedId);
                projet.setId(generatedId);
            } else {
                throw new SQLException("Échec de la création du projet, aucun ID généré.");
            }

            System.out.println("Projet enregistré avec succès : ");

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du projet : " + e.getMessage());
        }
        return projet;
    }

    @Override
    public Optional<Projet> findById(int id) {
        String sql = "SELECT * FROM projets WHERE id = ?";
        try(PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                Projet projet = new Projet();
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                projet.setId(rs.getInt("id"));
                projet.setNomProjet(rs.getString("nomProjet"));
                projet.setEtat(EtatProjet.valueOf(rs.getString("etat")));
                projet.setCoutTotal(rs.getDouble("couttotal"));
                projet.setMargeBeneficiaire(rs.getDouble("margeBeneficiaire"));
                projet.setClient(client);
                return Optional.of(projet);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Projet> findAll() {
        List<Projet> projets = new ArrayList<>();
        String sql = "SELECT * FROM projets";
        try (PreparedStatement ps=conn.prepareStatement(sql)){
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                Projet projet = new Projet();
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                projet.setId(rs.getInt("id"));
                projet.setNomProjet(rs.getString("nomProjet"));
                projet.setEtat(EtatProjet.valueOf(rs.getString("etat")));
                projet.setCoutTotal(rs.getDouble("couttotal"));
                projet.setMargeBeneficiaire(rs.getDouble("margeBeneficiaire"));
                projet.setClient(client);
                projets.add(projet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projets;
    }

    @Override
    public Projet update(Projet projet) {
        String sql = "UPDATE Projets SET nomprojet = ?, etat = ?::EtatProjet, surface = ? , client_id = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, projet.getNomProjet());
            preparedStatement.setString(4, projet.getEtat().name());
            preparedStatement.setDouble(5, projet.getSurface());
            preparedStatement.setInt(6, projet.getClient().getId());
            preparedStatement.setInt(7, projet.getId());

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("projet est mis à jour avec succes");

            if (affectedRows == 0) {
                throw new SQLException("La mise à jour du projet a échoué, aucune ligne affectée.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du projet : " + e.getMessage());
        }
        return projet;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Projets WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La suppression du projet a échoué, aucune ligne affectée.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du projet : " + e.getMessage());
        } finally {
            ConnectionConfig.closeConnection(conn);
        }
    }

    @Override
    public List<Projet> findByName(String nom) {
        String sql = "SELECT * FROM Projets WHERE nomprojet ILIKE ?";
        List<Projet> projets = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + nom + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getInt("client_id"));
                Projet projet = new Projet();
                projet.setId(resultSet.getInt("id"));
                projet.setNomProjet(resultSet.getString("nomprojet"));
                projet.setMargeBeneficiaire(resultSet.getDouble("margebeneficiaire"));
                projet.setCoutTotal(resultSet.getDouble("couttotal"));
                projet.setEtat(EtatProjet.valueOf(resultSet.getString("etat")));
                projet.setClient(client);

                projets.add(projet);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des projets par nom : " + e.getMessage());
        } finally {
            ConnectionConfig.closeConnection(conn);
        }

        return projets;
    }

}
