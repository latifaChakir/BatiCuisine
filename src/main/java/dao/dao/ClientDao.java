package dao.dao;

import bean.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDao {
    public Client save(Client client);
    public Optional<Client> findById(int id);
    public List<Client> findAll();
    public Client update(Client client);
    public void delete(int id);
    public List<Client> findByName(String name);
}