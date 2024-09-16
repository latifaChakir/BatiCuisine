package service;

import bean.Client;
import bean.Projet;
import dao.impl.ClientDaoImpl;
import dao.impl.ProjetDaoImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjetService {
    private ProjetDaoImpl projectDaoImpl;
    private Scanner scanner;
    public ProjetService() {
        this.scanner = new Scanner(System.in);
        this.projectDaoImpl=new ProjetDaoImpl();
    }
    public Projet save(Projet projet) {
        return this.projectDaoImpl.save(projet);
    }
    public Optional<Projet> findById(int projectId) {
        return this.projectDaoImpl.findById(projectId);
    }
    public List<Projet> findAll() {
        return this.projectDaoImpl.findAll();
    }
    public void delete(int projectId) {
        this.projectDaoImpl.delete(projectId);
    }
    public Projet update(Projet projet) {
        return this.projectDaoImpl.update(projet);
    }
    public List<Projet> findByName(String projetName) {
        return this.projectDaoImpl.findByName(projetName);
    }
    public void saveProjetClient(Projet projet, Client client) {
        ClientDaoImpl clientDaoImpl = new ClientDaoImpl();
        Client savedClient=clientDaoImpl.save(client);
        projet.setClient(savedClient);
        ProjetDaoImpl projectDaoImpl = new ProjetDaoImpl();
        projectDaoImpl.save(projet);
    }
    public void updateProjetClient(Projet projet, Client client) {
        ClientDaoImpl clientDaoImpl = new ClientDaoImpl();
        if (client.getId() == 0) {
            Client savedClient = clientDaoImpl.save(client);
            projet.setClient(savedClient);
        } else {
            Client updatedClient = clientDaoImpl.update(client);
            projet.setClient(updatedClient);
        }
        ProjetDaoImpl projectDaoImpl = new ProjetDaoImpl();
        projectDaoImpl.update(projet);
    }


}
