package dao.impl;

import bean.Composant;
import bean.MainOeuvre;
import bean.Materiau;
import bean.Projet;
import bean.enums.TypeComposant;
import config.ConnectionConfig;
import dao.dao.ComposantDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComposantDaoImpl implements ComposantDao {
    private Connection conn;
    public ComposantDaoImpl() {
        try {
            this.conn = ConnectionConfig.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Composant save(Composant composant) {
        String sql = "insert into composants(nom, typecomposant, tauxtva, projet_id) values(?, ?::TypeComposant, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, composant.getNom());
            ps.setString(2, composant.getTypeComposant().name());
            ps.setDouble(3, composant.getTauxTVA());
            ps.setInt(4, composant.getProjet().getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du composant, aucune ligne ajoutée.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int composantId = generatedKeys.getInt(1);
                    if (composant instanceof Materiau) {
                        Materiau materiau = (Materiau) composant;
                        String sqlMateriau = "INSERT INTO materiaux (composant_id, coutunitaire, quantite, couttransport, coefficientqualite) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement psMateriau = conn.prepareStatement(sqlMateriau)) {
                            psMateriau.setInt(1, composantId);
                            psMateriau.setDouble(2, materiau.getCoutUnitaire());
                            psMateriau.setDouble(3, materiau.getQuantite());
                            psMateriau.setDouble(4, materiau.getCoutTransport());
                            psMateriau.setDouble(5, materiau.getCoefficientQualite());
                            psMateriau.executeUpdate();
                        }
                    } else if (composant instanceof MainOeuvre) {
                        MainOeuvre mainOeuvre = (MainOeuvre) composant;
                        String sqlMainOeuvre = "INSERT INTO main_oeuvre (composant_id, tauxHoraire, heuresTravail, productiviteOuvrier) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement psMainOeuvre = conn.prepareStatement(sqlMainOeuvre)) {
                            psMainOeuvre.setInt(1, composantId);
                            psMainOeuvre.setDouble(2, mainOeuvre.getTauxHoraire());
                            psMainOeuvre.setDouble(3, mainOeuvre.getHeuresTravail());
                            psMainOeuvre.setDouble(4, mainOeuvre.getProductiviteOuvrier());
                            psMainOeuvre.executeUpdate();
                        }
                    }
                } else {
                    throw new SQLException("Échec de la création du composant, aucun ID obtenu.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return composant;
    }

    @Override
    public Optional<Composant> findById(int id) {
        String sql = "select * from composants where id = ?";
        try {
            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Composant composant = new Composant();
                Projet projet = new Projet();
                projet.setId(rs.getInt("projet_id"));
                composant.setId(rs.getInt("id"));
                composant.setNom(rs.getString("nom"));
                composant.setTauxTVA(rs.getDouble("tauxtva"));
                composant.setTypeComposant(TypeComposant.valueOf(rs.getString("typecomposant")));
                composant.setProjet(projet);
                return Optional.of(composant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Composant> findAll() {
        List<Composant> composants = new ArrayList<>();
        String sql = "select * from composants";
        try {
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                Composant composant = new Composant();
                Projet projet = new Projet();
                projet.setId(rs.getInt("projet_id"));
                composant.setId(rs.getInt("id"));
                composant.setNom(rs.getString("nom"));
                composant.setTauxTVA(rs.getDouble("tauxtva"));
                composant.setTypeComposant(TypeComposant.valueOf(rs.getString("typecomposant")));
                composant.setProjet(projet);
                composants.add(composant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return composants;
    }

    @Override
    public Composant update(Composant composant) {
        String sql = "UPDATE composants SET nom = ?, typecomposant = ?::TypeComposant, tauxtva = ?, projet_id = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, composant.getNom());
            ps.setString(2, composant.getTypeComposant().name());
            ps.setDouble(3, composant.getTauxTVA());
            ps.setInt(4, composant.getProjet().getId());
            ps.setInt(5, composant.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la mise à jour du composant, aucune ligne affectée.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return composant;
    }
    public void supprimerComposantsParProjet(int projetId) {
        String sql = "DELETE FROM Composants WHERE projet_id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, projetId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aucun composant n'a été supprimé pour le projet avec l'ID: " + projetId);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des composants : " + e.getMessage());
        }
    }
    public void mettreAJourComposantsDuProjet(Projet projet, List<Composant> nouveauxComposants) {
        supprimerComposantsParProjet(projet.getId());
        for (Composant composant : nouveauxComposants) {
            composant.setProjet(projet);
            save(composant);
        }

        System.out.println("Les composants du projet ont été mis à jour avec succès.");
    }



    @Override
    public void delete(int id) {
        String sql = "delete from composants where id = ?";
        try{
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Composant> findByName(String name) {
        List<Composant> composants = new ArrayList<>();
        String sql = "select * from composants where nom like ?";
        try {
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, "%"+name+"%");
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                Composant composant = new Composant();
                Projet projet = new Projet();
                projet.setId(rs.getInt("projet_id"));
                composant.setId(rs.getInt("id"));
                composant.setNom(rs.getString("nom"));
                composant.setTauxTVA(rs.getDouble("tauxtva"));
                composant.setTypeComposant(TypeComposant.valueOf(rs.getString("typecomposant")));
                composant.setProjet(projet);
                composants.add(composant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return composants;
    }


    public void update(Projet projet, List<Composant> nouveauxComposants) {
        try {
            String sqlDeleteMateriaux = "DELETE FROM materiaux WHERE composant_id IN (SELECT id FROM composants WHERE projet_id = ?)";
            String sqlDeleteMainOeuvre = "DELETE FROM main_oeuvre WHERE composant_id IN (SELECT id FROM composants WHERE projet_id = ?)";
            String sqlDeleteComposants = "DELETE FROM composants WHERE projet_id = ?";

            try (PreparedStatement psDeleteMateriaux = conn.prepareStatement(sqlDeleteMateriaux);
                 PreparedStatement psDeleteMainOeuvre = conn.prepareStatement(sqlDeleteMainOeuvre);
                 PreparedStatement psDeleteComposants = conn.prepareStatement(sqlDeleteComposants)) {

                psDeleteMateriaux.setInt(1, projet.getId());
                psDeleteMainOeuvre.setInt(1, projet.getId());
                psDeleteComposants.setInt(1, projet.getId());

                psDeleteMateriaux.executeUpdate();
                psDeleteMainOeuvre.executeUpdate();
                psDeleteComposants.executeUpdate();
            }

            for (Composant composant : nouveauxComposants) {
                save(composant);
            }

            System.out.println("Composants du projet " + projet.getNomProjet() + " mis à jour avec succès.");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour des composants du projet: " + e.getMessage(), e);
        }

}

    public List<Composant> findByProjet(Projet projet) {
        List<Composant> composants = new ArrayList<>();
        String query = "SELECT * FROM Composants WHERE projet_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, projet.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Composant composant = new Composant();
                composant.setId(resultSet.getInt("id"));
                composant.setNom(resultSet.getString("nom"));
                composant.setTypeComposant(TypeComposant.valueOf(resultSet.getString("typecomposant")));
                composant.setTauxTVA(resultSet.getDouble("tauxtva"));
                composant.setProjet(projet);
                composants.add(composant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return composants;
    }
}
