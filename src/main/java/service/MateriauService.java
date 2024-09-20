package service;

import bean.Materiau;
import dao.impl.MateriauDaoImpl;

import java.util.List;
import java.util.Optional;

public class MateriauService {
    private MateriauDaoImpl materiauDaoImpl;
    public MateriauService(){
        materiauDaoImpl = new MateriauDaoImpl();
    }
    public void save(Materiau Materiau) {
        materiauDaoImpl.save(Materiau);
    }

    public void update(Materiau Materiau) {
        materiauDaoImpl.update(Materiau);
    }

    public void delete(int id) {
        materiauDaoImpl.delete(id);
    }

    public Materiau findById(int id) {
        Optional<Materiau> optionalMateriau = materiauDaoImpl.findById(id);
        return optionalMateriau.orElse(null);
    }

    public List<Materiau> findAll() {
        return materiauDaoImpl.findAll();
    }
}
