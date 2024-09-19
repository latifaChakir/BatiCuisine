package dao.impl;

import bean.Composant;
import bean.Materiau;
import bean.Projet;
import bean.enums.TypeComposant;
import config.ConnectionConfig;
import dao.dao.MateriauDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MateriauDaoImpl implements MateriauDao {
    private Connection conn;
    public MateriauDaoImpl() {
        try {
            this.conn = ConnectionConfig.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Materiau save(Materiau materiau) {
        String sqlMateriau = "INSERT INTO materiaux (composant_id, coutunitaire, quantite, couttransport, coefficientqualite) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement psMateriau = conn.prepareStatement(sqlMateriau)) {
            psMateriau.setInt(1,materiau.getComposant().getId() );
            psMateriau.setDouble(2, materiau.getCoutUnitaire());
            psMateriau.setDouble(3, materiau.getQuantite());
            psMateriau.setDouble(4, materiau.getCoutTransport());
            psMateriau.setDouble(5, materiau.getCoefficientQualite());
            psMateriau.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Optional<Materiau> findById(int id) {
        String sql = "SELECT * FROM materiaux m " +
                "JOIN composants c ON m.composant_id = c.id " +
                "JOIN projets p ON c.projet_id = p.id " +
                "WHERE m.id = ?";
        Materiau materiau = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Projet projet = new Projet(
                            rs.getInt("p.id"),
                            rs.getString("nomprojet"),
                            rs.getDouble("couttotal")
                    );

                    Composant composant = new Composant(
                            rs.getInt("c.id"),
                            rs.getString("nom"),
                            TypeComposant.valueOf(rs.getString("typecomposant")),
                            rs.getDouble("tauxtva"),
                            projet
                    );

                    materiau = new Materiau();
                    materiau.setId(rs.getInt("m.id"));
                    materiau.setCoutUnitaire(rs.getDouble("coutunitaire"));
                    materiau.setQuantite(rs.getDouble("quantite"));
                    materiau.setCoutTransport(rs.getDouble("couttransport"));
                    materiau.setCoefficientQualite(rs.getDouble("coefficientqualite"));
                    materiau.setComposant(composant);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(materiau);
    }


    @Override
    public List<Materiau> findAll() {
        List<Materiau> materiaux = new ArrayList<>();
        String sql = "SELECT * FROM materiaux m join composants c on m.composant_id = c.id join projets p on c.projet_id = p.id where c.typecomposant='MATERIAU'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Projet projet=new Projet(
                            rs.getInt("p.id"),
                            rs.getString("nomprojet"),
                            rs.getDouble("couttotal")
                    );
                    Composant composant = new Composant(
                            rs.getInt("c.id"),
                            rs.getString("nom"),
                            TypeComposant.valueOf(rs.getString("typecomposant")),
                            rs.getDouble("tauxtva"),
                            projet
                    );

                    Materiau materiau = new Materiau();
                    materiau.setId(rs.getInt("m.id"));
                    materiau.setCoutUnitaire(rs.getDouble("coutunitaire"));
                    materiau.setQuantite(rs.getDouble("quantite"));
                    materiau.setCoutTransport(rs.getDouble("couttransport"));
                    materiau.setCoefficientQualite(rs.getDouble("coefficientqualite"));
                    materiau.setComposant(composant);
                    materiaux.add(materiau);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return materiaux;
    }


    @Override
    public Materiau update(Materiau materiau) {
        String sql = "UPDATE materiaux SET coutunitaire = ?, quantite = ?, couttransport = ?, coefficientqualite = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, materiau.getCoutUnitaire());
            ps.setDouble(2, materiau.getQuantite());
            ps.setDouble(3, materiau.getCoutTransport());
            ps.setDouble(4, materiau.getCoefficientQualite());
            ps.setInt(5, materiau.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return materiau;
    }


    @Override
    public void delete(int id) {
        String sql = "DELETE FROM materiaux WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
