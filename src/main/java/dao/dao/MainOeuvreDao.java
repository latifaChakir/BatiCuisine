package dao.dao;

import bean.MainOeuvre;

import java.util.List;
import java.util.Optional;

public interface MainOeuvreDao {
    public MainOeuvre save(MainOeuvre MainOeuvre);
    public Optional<MainOeuvre> findById(int id);
    public List<MainOeuvre> findAll();
    public MainOeuvre update(MainOeuvre MainOeuvre);
    public void delete(int id);
}
