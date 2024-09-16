package service;

import bean.Client;
import dao.impl.ClientDaoImpl;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private ClientDaoImpl clientDaoImpl;

    public ClientService() {
        clientDaoImpl = new ClientDaoImpl();
    }

    public void save(Client client) {
        clientDaoImpl.save(client);
    }

    public void update(Client client) {
        clientDaoImpl.update(client);
    }

    public void delete(int id) {
        clientDaoImpl.delete(id);
    }

    public Client findById(int id) {
        Optional<Client> optionalClient = clientDaoImpl.findById(id);
        return optionalClient.orElse(null);
    }

    public List<Client> findAll() {
        return clientDaoImpl.findAll();
    }

    public List<Client> findByNom(String nomClient) {
        return clientDaoImpl.findByName(nomClient);
    }
}
