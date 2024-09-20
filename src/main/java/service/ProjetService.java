package service;

import bean.Client;
import bean.Composant;
import bean.Projet;
import dao.impl.ClientDaoImpl;
import dao.impl.ComposantDaoImpl;
import dao.impl.ProjetDaoImpl;
import java.util.List;
import java.util.Optional;

public class ProjetService {
    private ProjetDaoImpl projectDaoImpl;
    private ComposantDaoImpl composantDaoImpl;
    public ProjetService() {
        this.composantDaoImpl=new ComposantDaoImpl();
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
        Projet updatedProjet = this.projectDaoImpl.update(projet);
        List<Composant> composants = composantDaoImpl.findByProjet(updatedProjet);
        composantDaoImpl.mettreAJourComposantsDuProjet(updatedProjet, composants);
        return updatedProjet;
    }
    public Projet miseAJourProjet(Projet projet) {
        return this.projectDaoImpl.updateTotal(projet);
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
    public void saveProjetClientFound(Projet projet, Client client) {
        projet.setClient(client);
        ProjetDaoImpl projectDaoImpl = new ProjetDaoImpl();
        projectDaoImpl.save(projet);
    }


}
