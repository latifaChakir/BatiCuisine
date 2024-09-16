package dao.impl;

import bean.Composant;
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
        String sql = "insert into composants(nom,typecomposant,tauxtva,projet_id) values(?,?::TupeComposant,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, composant.getNom());
            ps.setString(2,composant.getTypeComposant().name());
            ps.setDouble(3,composant.getTauxTVA());
            ps.setInt(4, composant.getProjet().getId());
            ps.executeUpdate();
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
        String sql="UPDATE composants SET nom=?,typecomposant=?::TupeComposant,tauxtva=?,projet_id=? where id=?";
        try {
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, composant.getNom());
            ps.setString(2, composant.getTypeComposant().name());
            ps.setDouble(3, composant.getTauxTVA());
            ps.setInt(4, composant.getProjet().getId());
            ps.setInt(5, composant.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return composant;
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
}
