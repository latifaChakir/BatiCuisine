package dao.dao;

import bean.Projet;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProjetDao {
    public Projet save(Projet projet);
    public Optional<Projet> findById(int id);
    public List<Projet> findAll();
    public Projet update(Projet projet);
    public void delete(int id) throws SQLException;
    public List<Projet> findByName(String name);
}
