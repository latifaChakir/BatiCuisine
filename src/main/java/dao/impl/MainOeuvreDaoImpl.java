package dao.impl;

import bean.Composant;
import bean.MainOeuvre;
import bean.Projet;
import bean.enums.TypeComposant;
import config.ConnectionConfig;
import dao.dao.MainOeuvreDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainOeuvreDaoImpl implements MainOeuvreDao {
    private Connection conn;
    public MainOeuvreDaoImpl() {
        try {
            this.conn = ConnectionConfig.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public MainOeuvre save(MainOeuvre mainOeuvre) {
        String sqlMainOeuvre = "INSERT INTO main_oeuvre (composant_id, tauxHoraire, heuresTravail, productiviteOuvrier) VALUES (?, ?, ?, ?)";
        try (PreparedStatement psMainOeuvre = conn.prepareStatement(sqlMainOeuvre)) {
            psMainOeuvre.setInt(1, mainOeuvre.getComposant().getId());
            psMainOeuvre.setDouble(2, mainOeuvre.getTauxHoraire());
            psMainOeuvre.setDouble(3, mainOeuvre.getHeuresTravail());
            psMainOeuvre.setDouble(4, mainOeuvre.getProductiviteOuvrier());
            psMainOeuvre.executeUpdate();
            return mainOeuvre;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<MainOeuvre> findById(int id) {
        String sql = "SELECT * FROM main_oeuvre m " +
                "JOIN composants c ON m.composant_id = c.id " +
                "WHERE m.id = ?";
        MainOeuvre mainOeuvre = null;

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

                    mainOeuvre = new MainOeuvre();
                    mainOeuvre.setId(rs.getInt("m.id"));
                    mainOeuvre.setTauxHoraire(rs.getDouble("tauxHoraire"));
                    mainOeuvre.setHeuresTravail(rs.getDouble("heuresTravail"));
                    mainOeuvre.setProductiviteOuvrier(rs.getDouble("productiviteOuvrier"));
                    mainOeuvre.setComposant(composant);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(mainOeuvre);
    }


    @Override
    public List<MainOeuvre> findAll() {
        List<MainOeuvre> mainOeuvres = new ArrayList<>();
        String sql = "SELECT * FROM main_oeuvre m " +
                "JOIN composants c ON m.composant_id = c.id" +
                "  join projets p on c.projet_id = p.id " +
                "where c.typecomposant='MainDOeuvre' ";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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

                    MainOeuvre mainOeuvre = new MainOeuvre();
                    mainOeuvre.setId(rs.getInt("m.id"));
                    mainOeuvre.setTauxHoraire(rs.getDouble("tauxHoraire"));
                    mainOeuvre.setHeuresTravail(rs.getDouble("heuresTravail"));
                    mainOeuvre.setProductiviteOuvrier(rs.getDouble("productiviteOuvrier"));
                    mainOeuvre.setComposant(composant);

                    mainOeuvres.add(mainOeuvre);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mainOeuvres;
    }


    @Override
    public MainOeuvre update(MainOeuvre mainOeuvre) {
        String sql = "UPDATE main_oeuvre SET tauxHoraire = ?, heuresTravail = ?, productiviteOuvrier = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, mainOeuvre.getTauxHoraire());
            ps.setDouble(2, mainOeuvre.getHeuresTravail());
            ps.setDouble(3, mainOeuvre.getProductiviteOuvrier());
            ps.setInt(4, mainOeuvre.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mainOeuvre;
    }


    @Override
    public void delete(int id) {
        String sql = "DELETE FROM main_oeuvre WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
