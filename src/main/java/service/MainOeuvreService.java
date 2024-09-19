package service;

import bean.MainOeuvre;
import dao.impl.MainOeuvreDaoImpl;

import java.util.List;
import java.util.Optional;

public class MainOeuvreService {
    private MainOeuvreDaoImpl mainOeuvreDaoImpl;
    public MainOeuvreService(){
        this.mainOeuvreDaoImpl = new MainOeuvreDaoImpl();
    }

    public void save(MainOeuvre MainOeuvre) {
        mainOeuvreDaoImpl.save(MainOeuvre);
    }

    public void update(MainOeuvre MainOeuvre) {
        mainOeuvreDaoImpl.update(MainOeuvre);
    }

    public void delete(int id) {
        mainOeuvreDaoImpl.delete(id);
    }

    public MainOeuvre findById(int id) {
        Optional<MainOeuvre> optionalMainOeuvre = mainOeuvreDaoImpl.findById(id);
        return optionalMainOeuvre.orElse(null);
    }

    public List<MainOeuvre> findAll() {
        return mainOeuvreDaoImpl.findAll();
    }
}
