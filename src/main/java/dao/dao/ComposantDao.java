package dao.dao;

import bean.Composant;

import java.util.List;
import java.util.Optional;

public interface ComposantDao {
    public Composant save(Composant composant);
    public Optional<Composant> findById(int id);
    public List<Composant> findAll();
    public Composant update(Composant composant);
    public void delete(int id);
    public List<Composant> findByName(String name);
}
