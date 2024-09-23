package dao.impl;

import bean.*;
import bean.enums.EtatProjet;
import bean.enums.TypeComposant;
import config.ConnectionConfig;
import dao.dao.DevisDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class DevisDaoImpl implements DevisDao {
    public DevisDaoImpl() {

    }

    @Override
    public Devis save(Devis devis) {
        String sql = "INSERT INTO devis (montantestime, dateemission, datevalidite, accepte, projet_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionConfig.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
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
        String sql = "SELECT d.id AS devis_id, d.montantestime AS devis_montantEstime, d.dateemission AS devis_dateEmission, d.datevalidite AS devis_dateValidite, d.accepte AS devis_accepte, " +
                "p.id AS projet_id, p.nomProjet AS projet_nom, p.etat AS projet_etat, p.coutTotal AS projet_coutTotal, p.margeBeneficiaire AS projet_margeBeneficiaire, " +
                "cl.id AS client_id, cl.nom AS client_nom, cl.adresse AS client_adresse, cl.telephone AS client_telephone, cl.estProfessionnel AS client_estProfessionnel, " +
                "c.id AS composant_id, c.nom AS composant_nom, c.typecomposant AS composant_type, c.tauxtva AS composant_tauxtva, " +
                "m.id AS materiau_id, m.coutunitaire AS materiau_coutunitaire, m.quantite AS materiau_quantite, m.couttransport AS materiau_couttransport, m.coefficientqualite AS materiau_coefficientqualite, " +
                "mo.id AS main_oeuvre_id, mo.heuresTravail AS main_oeuvre_heuresTravail, mo.tauxHoraire AS main_oeuvre_tauxHoraire, mo.productiviteouvrier AS main_oeuvre_productivite " +
                "FROM devis d " +
                "JOIN projets p ON d.projet_id = p.id " +
                "JOIN clients cl ON p.client_id = cl.id " +
                "JOIN composants c ON c.projet_id = p.id " +
                "LEFT JOIN materiaux m ON m.composant_id = c.id " +
                "LEFT JOIN main_oeuvre mo ON mo.composant_id = c.id " +
                "WHERE d.id = ?";
        try (Connection conn = ConnectionConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Client
                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    client.setNom(rs.getString("client_nom"));
                    client.setAdresse(rs.getString("client_adresse"));
                    client.setTelephone(rs.getString("client_telephone"));
                    client.setEstProfessionnel(rs.getBoolean("client_estProfessionnel"));

                    // Projet
                    Projet projet = new Projet();
                    projet.setId(rs.getInt("projet_id"));
                    projet.setNomProjet(rs.getString("projet_nom"));
                    projet.setEtat(EtatProjet.valueOf(rs.getString("projet_etat")));
                    projet.setCoutTotal(rs.getDouble("projet_coutTotal"));
                    projet.setMargeBeneficiaire(rs.getDouble("projet_margeBeneficiaire"));
                    projet.setClient(client);
                    projet.setComposants(new ArrayList<>());

                    // Devis
                    Devis devis = new Devis();
                    devis.setId(rs.getLong("devis_id"));
                    devis.setEstimatedAmount(rs.getDouble("devis_montantEstime"));
                    devis.setIssueDate(rs.getDate("devis_dateEmission").toLocalDate());
                    devis.setValidatedDate(rs.getDate("devis_dateValidite") != null ? rs.getDate("devis_dateValidite").toLocalDate() : null);
                    devis.setAccepted(rs.getBoolean("devis_accepte"));
                    devis.setProjet(projet);

                    // Composant
                    do {
                        Composant composant = new Composant();
                        composant.setId(rs.getInt("composant_id"));
                        composant.setNom(rs.getString("composant_nom"));
                        composant.setTypeComposant(TypeComposant.valueOf(rs.getString("composant_type")));
                        composant.setTauxTVA(rs.getDouble("composant_tauxtva"));
                        composant.setProjet(devis.getProjet());
                        composant.setMateriaux(new ArrayList<>());
                        composant.setMainOeuvres(new ArrayList<>());

                        // Add Materiau or MainOeuvre
                        if ("Materiel".equals(rs.getString("composant_type"))) {
                            Materiau materiau = new Materiau();
                            materiau.setId(rs.getInt("materiau_id"));
                            materiau.setCoutUnitaire(rs.getDouble("materiau_coutunitaire"));
                            materiau.setQuantite(rs.getDouble("materiau_quantite"));
                            materiau.setCoutTransport(rs.getDouble("materiau_couttransport"));
                            materiau.setCoefficientQualite(rs.getDouble("materiau_coefficientqualite"));
                            composant.getMateriaux().add(materiau);
                        }

                        if ("MainDOeuvre".equals(rs.getString("composant_type"))) {
                            MainOeuvre mainOeuvre = new MainOeuvre();
                            mainOeuvre.setId(rs.getInt("main_oeuvre_id"));
                            mainOeuvre.setHeuresTravail(rs.getDouble("main_oeuvre_heuresTravail"));
                            mainOeuvre.setTauxHoraire(rs.getDouble("main_oeuvre_tauxHoraire"));
                            mainOeuvre.setProductiviteOuvrier(rs.getDouble("main_oeuvre_productivite"));
                            composant.getMainOeuvres().add(mainOeuvre);
                        }
                        devis.getProjet().getComposants().add(composant);
                    } while (rs.next());

                    return Optional.of(devis);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching devis by ID", e);
        }
        return Optional.empty();
    }
    @Override
    public List<Devis> findAll() {
        String sql = "SELECT d.id AS devis_id, d.montantestime AS devis_montantEstime, d.dateemission AS devis_dateEmission, d.datevalidite AS devis_dateValidite, d.accepte AS devis_accepte, " +
                "p.id AS projet_id, p.nomProjet AS projet_nom, p.etat AS projet_etat, p.coutTotal AS projet_coutTotal, p.margeBeneficiaire AS projet_margeBeneficiaire, " +
                "cl.id AS client_id, cl.nom AS client_nom, cl.adresse AS client_adresse, cl.telephone AS client_telephone, cl.estProfessionnel AS client_estProfessionnel, " +
                "c.id AS composant_id, c.nom AS composant_nom, c.typecomposant AS composant_type, c.tauxtva AS composant_tauxtva, " +
                "m.id AS materiau_id, m.coutunitaire AS materiau_coutunitaire, m.quantite AS materiau_quantite, m.couttransport AS materiau_couttransport, m.coefficientqualite AS materiau_coefficientqualite, " +
                "mo.id AS main_oeuvre_id, mo.heuresTravail AS main_oeuvre_heuresTravail, mo.tauxHoraire AS main_oeuvre_tauxHoraire, mo.productiviteouvrier AS main_oeuvre_productivite " +
                "FROM devis d " +
                "JOIN projets p ON d.projet_id = p.id " +
                "JOIN clients cl ON p.client_id = cl.id " +
                "JOIN composants c ON c.projet_id = p.id " +
                "LEFT JOIN materiaux m ON m.composant_id = c.id " +
                "LEFT JOIN main_oeuvre mo ON mo.composant_id = c.id";

        Map<Long, Devis> devisMap = new HashMap<>();
        try (Connection conn = ConnectionConfig.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long devisId = rs.getLong("devis_id");
                Devis devis = devisMap.get(devisId);

                if (devis == null) {
                    // Client
                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    client.setNom(rs.getString("client_nom"));
                    client.setAdresse(rs.getString("client_adresse"));
                    client.setTelephone(rs.getString("client_telephone"));
                    client.setEstProfessionnel(rs.getBoolean("client_estProfessionnel"));

                    // Projet
                    Projet projet = new Projet();
                    projet.setId(rs.getInt("projet_id"));
                    projet.setNomProjet(rs.getString("projet_nom"));
                    projet.setEtat(EtatProjet.valueOf(rs.getString("projet_etat")));
                    projet.setCoutTotal(rs.getDouble("projet_coutTotal"));
                    projet.setMargeBeneficiaire(rs.getDouble("projet_margeBeneficiaire"));
                    projet.setClient(client);
                    projet.setComposants(new ArrayList<>());

                    // Devis
                    devis = new Devis();
                    devis.setId(devisId);
                    devis.setEstimatedAmount(rs.getDouble("devis_montantEstime"));
                    devis.setIssueDate(rs.getDate("devis_dateEmission").toLocalDate());
                    devis.setValidatedDate(rs.getDate("devis_dateValidite") != null ? rs.getDate("devis_dateValidite").toLocalDate() : null);
                    devis.setAccepted(rs.getBoolean("devis_accepte"));
                    devis.setProjet(projet);

                    devisMap.put(devisId, devis);
                }

                // Composant
                Composant composant = new Composant();
                composant.setId(rs.getInt("composant_id"));
                composant.setNom(rs.getString("composant_nom"));
                composant.setTypeComposant(TypeComposant.valueOf(rs.getString("composant_type")));
                composant.setTauxTVA(rs.getDouble("composant_tauxtva"));
                composant.setProjet(devis.getProjet());
                composant.setMateriaux(new ArrayList<>());
                composant.setMainOeuvres(new ArrayList<>());

                // Add Materiau or MainOeuvre
                if ("Materiel".equals(rs.getString("composant_type"))) {
                    Materiau materiau = new Materiau();
                    materiau.setId(rs.getInt("materiau_id"));
                    materiau.setCoutUnitaire(rs.getDouble("materiau_coutunitaire"));
                    materiau.setQuantite(rs.getDouble("materiau_quantite"));
                    materiau.setCoutTransport(rs.getDouble("materiau_couttransport"));
                    materiau.setCoefficientQualite(rs.getDouble("materiau_coefficientqualite"));
                    composant.getMateriaux().add(materiau);
                }

                if ("MainDOeuvre".equals(rs.getString("composant_type"))) {
                    MainOeuvre mainOeuvre = new MainOeuvre();
                    mainOeuvre.setId(rs.getInt("main_oeuvre_id"));
                    mainOeuvre.setHeuresTravail(rs.getDouble("main_oeuvre_heuresTravail"));
                    mainOeuvre.setTauxHoraire(rs.getDouble("main_oeuvre_tauxHoraire"));
                    mainOeuvre.setProductiviteOuvrier(rs.getDouble("main_oeuvre_productivite"));
                    composant.getMainOeuvres().add(mainOeuvre);
                }

                // Add Composant to Projet
                devis.getProjet().getComposants().add(composant);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all devis", e);
        }
        return new ArrayList<>(devisMap.values());
    }

    @Override
    public Devis update(Devis devis) {
        String sql = "UPDATE devis SET montantestime = ?, dateemission = ?, datevalidite = ?, accepte = ?, projet_id = ? WHERE id = ?";
        try (Connection conn = ConnectionConfig.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, devis.getEstimatedAmount());
            ps.setDate(2, java.sql.Date.valueOf(devis.getIssueDate()));
            ps.setDate(3, java.sql.Date.valueOf(devis.getValidatedDate()));
            ps.setBoolean(4, devis.isAccepted());
            ps.setLong(5, devis.getProjet().getId());
            ps.setLong(6, devis.getId());
//            boolean isAccepted = devis.isAccepted();
//            if (validatedDate != null && validatedDate.isBefore(LocalDate.now())) {
//                isAccepted = false;
//            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating devis failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating devis", e);
        }
        return devis;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM devis WHERE id = ?";
        try (Connection conn = ConnectionConfig.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
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
