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
    public Projet findById() {
        System.out.println("enter the id of the projet");
        int projectId = scanner.nextInt();
        Optional<Projet> projet = this.projectDaoImpl.findById(projectId);
        if (projet.isPresent()) {
            return projet.get();
        }else{
            return null;
        }
    }
    public List<Projet> findAll() {
        return this.projectDaoImpl.findAll();
    }
    public void delete() {
        System.out.println("enter the id of the projet");
        int projectId = scanner.nextInt();
        this.projectDaoImpl.delete(projectId);
    }
    public Projet update(Projet projet) {
        return this.projectDaoImpl.update(projet);
    }
    public List<Projet> findByName() {
        System.out.println("enter the name of the projet");
        String projetName = scanner.next();
        return this.projectDaoImpl.findByName(projetName);
    }
    public void saveProjetClient(Projet projet, Client client) {
        ClientDaoImpl clientDaoImpl = new ClientDaoImpl();
        clientDaoImpl.save(client);
        projet.setClient(client);
        projectDaoImpl.save(projet);
    }

}
