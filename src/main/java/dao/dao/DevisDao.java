package dao.dao;

import bean.Devis;

import java.util.List;
import java.util.Optional;

public interface DevisDao {
    public Devis save(Devis Devis);
    public Optional<Devis> findById(int id);
    public List<Devis> findAll();
    public Devis update(Devis Devis);
    public void delete(int id);
}
