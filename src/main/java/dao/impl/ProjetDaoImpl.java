package dao.impl;

import bean.*;
import bean.enums.EtatProjet;
import bean.enums.TypeComposant;
import config.ConnectionConfig;
import dao.dao.ProjetDao;
import exceptions.ProjectNotFoundException;

import java.sql.*;
import java.util.*;

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
        String sql = "SELECT p.id AS projet_id,p.surface as projet_surface, p.nomProjet AS projet_nom, p.etat AS projet_etat, p.coutTotal AS projet_coutTotal, p.margeBeneficiaire AS projet_margeBeneficiaire, " +
                "cl.id AS client_id, cl.nom AS client_nom, cl.adresse AS client_adresse, cl.telephone AS client_telephone, " +
                "c.id AS composant_id, c.nom AS composant_nom, c.typecomposant AS composant_type, c.tauxtva AS composant_tauxtva, " +
                "m.id AS materiau_id, m.coutunitaire AS materiau_coutunitaire, m.quantite AS materiau_quantite, m.couttransport AS materiau_couttransport, m.coefficientqualite AS materiau_coefficientqualite, " +
                "mo.id AS main_oeuvre_id, mo.heuresTravail AS main_oeuvre_heuresTravail, mo.tauxHoraire AS main_oeuvre_tauxHoraire, mo.productiviteouvrier AS main_oeuvre_productivite " +
                "FROM projets p " +
                "JOIN clients cl ON p.client_id = cl.id " +
                "JOIN composants c ON c.projet_id = p.id " +
                "LEFT JOIN materiaux m ON m.composant_id = c.id " +
                "LEFT JOIN main_oeuvre mo ON mo.composant_id = c.id " +
                "WHERE p.id = ?";

        Map<Integer, Projet> projetMap = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int projetId = rs.getInt("projet_id");
                    Projet projet = projetMap.get(projetId);

                    if (projet == null) {
                        Client client = new Client();
                        client.setId(rs.getInt("client_id"));
                        client.setNom(rs.getString("client_nom"));
                        client.setAdresse(rs.getString("client_adresse"));
                        client.setTelephone(rs.getString("client_telephone"));

                        projet = new Projet();
                        projet.setId(projetId);
                        projet.setNomProjet(rs.getString("projet_nom"));
                        projet.setSurface(rs.getDouble("projet_surface"));
                        projet.setEtat(EtatProjet.valueOf(rs.getString("projet_etat")));
                        projet.setCoutTotal(rs.getDouble("projet_coutTotal"));
                        projet.setMargeBeneficiaire(rs.getDouble("projet_margeBeneficiaire"));
                        projet.setClient(client);
                        projet.setComposants(new ArrayList<>());

                        projetMap.put(projetId, projet);
                    }

                    Composant composant = new Composant();
                    composant.setMateriaux(new ArrayList<>());
                    composant.setMainOeuvres(new ArrayList<>());
                    composant.setId(rs.getInt("composant_id"));
                    composant.setNom(rs.getString("composant_nom"));
                    composant.setTypeComposant(TypeComposant.valueOf(rs.getString("composant_type")));
                    composant.setTauxTVA(rs.getDouble("composant_tauxtva"));
                    composant.setProjet(projet);

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

                    projet.getComposants().add(composant);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return projetMap.isEmpty() ? Optional.empty() : Optional.of(projetMap.values().iterator().next());
    }

    @Override
    public List<Projet> findAll() {
        Map<Integer, Projet> projetMap = new HashMap<>();
        String sql = "SELECT p.id AS projet_id, p.nomProjet AS projet_nom, p.etat AS projet_etat, p.coutTotal AS projet_coutTotal, p.margeBeneficiaire AS projet_margeBeneficiaire, " +
                "cl.id AS client_id, cl.nom AS client_nom, cl.adresse AS client_adresse, cl.telephone AS client_telephone, " +
                "c.id AS composant_id, c.nom AS composant_nom, c.typecomposant AS composant_type, c.tauxtva AS composant_tauxtva, " +
                "m.id AS materiau_id, m.coutunitaire AS materiau_coutunitaire, m.quantite AS materiau_quantite, m.couttransport AS materiau_couttransport, m.coefficientqualite AS materiau_coefficientqualite ,  " +
                "mo.id AS main_oeuvre_id, mo.heuresTravail AS main_oeuvre_heuresTravail, mo.tauxHoraire AS main_oeuvre_tauxHoraire , mo.productiviteouvrier AS main_oeuvre_productivite " +
                "FROM projets p " +
                "JOIN clients cl ON p.client_id = cl.id " +
                "JOIN composants c ON c.projet_id = p.id " +
                "LEFT JOIN materiaux m ON m.composant_id = c.id " +
                "LEFT JOIN main_oeuvre mo ON mo.composant_id = c.id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int projetId = rs.getInt("projet_id");
                    Projet projet = projetMap.get(projetId);

                    if (projet == null) {
                        Client client = new Client();
                        client.setId(rs.getInt("client_id"));
                        client.setNom(rs.getString("client_nom"));
                        client.setAdresse(rs.getString("client_adresse"));
                        client.setTelephone(rs.getString("client_telephone"));

                        projet = new Projet();
                        projet.setId(projetId);
                        projet.setNomProjet(rs.getString("projet_nom"));
                        projet.setEtat(EtatProjet.valueOf(rs.getString("projet_etat")));
                        projet.setCoutTotal(rs.getDouble("projet_coutTotal"));
                        projet.setMargeBeneficiaire(rs.getDouble("projet_margeBeneficiaire"));
                        projet.setClient(client);
                        projet.setComposants(new ArrayList<>());

                        projetMap.put(projetId, projet);
                    }

                    Composant composant = new Composant();
                    composant.setMateriaux(new ArrayList<>());
                    composant.setMainOeuvres(new ArrayList<>());
                    composant.setId(rs.getInt("composant_id"));
                    composant.setNom(rs.getString("composant_nom"));
                    composant.setTypeComposant(TypeComposant.valueOf(rs.getString("composant_type")));
                    composant.setTauxTVA(rs.getDouble("composant_tauxtva"));
                    composant.setProjet(projet);

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
                        composant.getMainOeuvres().add(mainOeuvre);                    }

                    projet.getComposants().add(composant);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>(projetMap.values());
    }

    public Projet updateTotal(Projet projet) {
        String sql = "UPDATE Projets SET coutTotal = ?, margebeneficiaire=?  WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setDouble(1, projet.getCoutTotal());
            preparedStatement.setDouble(2, projet.getMargeBeneficiaire());
            preparedStatement.setInt(3, projet.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La mise à jour du projet a échoué, aucune ligne affectée.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du projet : " + e.getMessage());
        }
        return projet;
    }
    @Override
    public Projet update(Projet projet) {
        String sql = "UPDATE Projets SET nomprojet = ?, etat = ?::EtatProjet, surface = ? , client_id = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, projet.getNomProjet());
            preparedStatement.setString(2, projet.getEtat().name());
            preparedStatement.setDouble(3, projet.getSurface());
            preparedStatement.setInt(4, projet.getClient().getId());
            preparedStatement.setInt(5, projet.getId());

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
        String sql = "DELETE FROM projets WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Project Supprimé");
            } else {
                throw new ProjectNotFoundException("Delete failed, project not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
