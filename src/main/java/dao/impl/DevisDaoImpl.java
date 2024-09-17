package dao.impl;

import bean.Client;
import bean.Devis;
import bean.Projet;
import bean.enums.EtatProjet;
import config.ConnectionConfig;
import dao.dao.DevisDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DevisDaoImpl implements DevisDao {
    private Connection conn;
    public DevisDaoImpl() {
        try {
            this.conn = ConnectionConfig.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Devis save(Devis devis) {
        String sql = "INSERT INTO devis (montantestime, dateemission, datevalidite, accepte, projet_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, devis.getEstimatedAmount());
            ps.setDate(2, java.sql.Date.valueOf(devis.getIssueDate()));
            ps.setDate(3, (devis.getValidatedDate() != null) ? java.sql.Date.valueOf(devis.getValidatedDate()) : null);
            ps.setBoolean(4, devis.isAccepted());
            ps.setLong(5, devis.getProjet().getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du devis, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    devis.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Échec de la création du devis, aucun ID généré.");
                }
            }

            System.out.println("Devis ajouté avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du devis : " + e.getMessage());
        }
        return devis;
    }

    @Override
    public Optional<Devis> findById(int id) {
        String sql = "SELECT *, projets.id as projet_id,clients.id as client_id FROM devis " +
                "JOIN projets ON devis.projet_id = projets.id " +
                "JOIN clients ON projets.client_id = clients.id " +
                "WHERE devis.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client(
                            rs.getInt("client_id"),
                            rs.getString("nom"),
                            rs.getString("adresse"),
                            rs.getString("telephone"),
                            rs.getBoolean("estProfessionnel")
                    );
                    Projet projet = new Projet(
                            rs.getInt("projet_id"),
                            rs.getString("nomprojet"),
                            rs.getDouble("margebeneficiaire"),
                            rs.getDouble("couttotal"),
                            EtatProjet.valueOf(rs.getString("etat")),                            client,
                            rs.getDouble("surface")
                    );
                    Devis devis = new Devis();
                    devis.setId(rs.getLong("id"));
                    devis.setEstimatedAmount(rs.getDouble("montantestime"));
                    devis.setIssueDate(rs.getDate("dateemission").toLocalDate());
                    devis.setValidatedDate(rs.getDate("datevalidite").toLocalDate());
                    devis.setAccepted(rs.getBoolean("accepte"));
                    devis.setProjet(projet);
                    return Optional.of(devis);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public List<Devis> findAll() {
        List<Devis> devisList = new ArrayList<>();
        String sql = "SELECT *,projets.id as projet_id,clients.id as client_id FROM devis " +
                "JOIN projets ON devis.projet_id = projets.id " +
                "JOIN clients ON projets.client_id = clients.id";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("client_id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getBoolean("estProfessionnel")
                );
                Projet projet = new Projet(
                        rs.getInt("projet_id"),
                        rs.getString("nomprojet"),
                        rs.getDouble("margebeneficiaire"),
                        rs.getDouble("couttotal"),
                        EtatProjet.valueOf(rs.getString("etat")),
                        client,
                        rs.getDouble("surface")
                );
                Devis devis = new Devis();
                devis.setId(rs.getLong("id"));
                devis.setEstimatedAmount(rs.getDouble("montantestime"));
                devis.setIssueDate(rs.getDate("dateemission").toLocalDate());
                devis.setValidatedDate(rs.getDate("datevalidite") != null ? rs.getDate("datevalidite").toLocalDate() : null);
                devis.setAccepted(rs.getBoolean("accepte"));
                devis.setProjet(projet);

                devisList.add(devis);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all devis", e);
        }
        return devisList;
    }

    @Override
    public Devis update(Devis devis) {
        String sql = "UPDATE devis SET montantestime = ?, dateemission = ?, datevalidite = ?, accepte = ?, projet_id = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, devis.getEstimatedAmount());
            ps.setDate(2, java.sql.Date.valueOf(devis.getIssueDate()));
            ps.setDate(3, devis.getValidatedDate() != null ? java.sql.Date.valueOf(devis.getValidatedDate()) : null);
            ps.setBoolean(4, devis.isAccepted());
            ps.setLong(5, devis.getProjet().getId());
            ps.setLong(6, devis.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating devis failed, no rows affected.");
            }

            System.out.println("Devis updated successfully: " + devis);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating devis", e);
        }
        return devis;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM devis WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting devis failed, no rows affected.");
            }

            System.out.println("Devis deleted successfully, id: " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting devis", e);
        }
    }
}
