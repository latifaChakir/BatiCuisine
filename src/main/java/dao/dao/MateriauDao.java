package dao.dao;

import bean.Materiau;

import java.util.List;
import java.util.Optional;

public interface MateriauDao {
    public Materiau save(Materiau Materiau);
    public Optional<Materiau> findById(int id);
    public List<Materiau> findAll();
    public Materiau update(Materiau Materiau);
    public void delete(int id);
}
