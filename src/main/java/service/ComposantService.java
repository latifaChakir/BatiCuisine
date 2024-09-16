package service;

import bean.Composant;
import dao.impl.ComposantDaoImpl;

import java.util.List;
import java.util.Optional;

public class ComposantService {
    private ComposantDaoImpl composantDaoImpl;
    public ComposantService() {
        this.composantDaoImpl = new ComposantDaoImpl();
    }
    public void save(Composant Composant) {
        composantDaoImpl.save(Composant);
    }

    public void update(Composant Composant) {
        composantDaoImpl.update(Composant);
    }

    public void delete(int id) {
        composantDaoImpl.delete(id);
    }

    public Composant findById(int id) {
        Optional<Composant> optionalComposant = composantDaoImpl.findById(id);
        return optionalComposant.orElse(null);
    }

    public List<Composant> findAll() {
        return composantDaoImpl.findAll();
    }

    public List<Composant> findByNom(String nomComposant) {
        return composantDaoImpl.findByName(nomComposant);
    }
}
