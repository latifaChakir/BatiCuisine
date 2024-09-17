package service;

import bean.Devis;
import dao.impl.DevisDaoImpl;

import java.util.List;
import java.util.Optional;

public class DevisService {
    private DevisDaoImpl devisDaoImpl;
    public DevisService() {
        this.devisDaoImpl = new DevisDaoImpl();
    }
    public void save(Devis Devis) {
        devisDaoImpl.save(Devis);
    }

    public void update(Devis Devis) {
        devisDaoImpl.update(Devis);
    }

    public void delete(int id) {
        devisDaoImpl.delete(id);
    }

    public Devis findById(int id) {
        Optional<Devis> optionalDevis = devisDaoImpl.findById(id);
        return optionalDevis.orElse(null);
    }

    public List<Devis> findAll() {
        return devisDaoImpl.findAll();
    }

}
