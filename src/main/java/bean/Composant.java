package bean;

import bean.enums.TypeComposant;

import java.util.ArrayList;
import java.util.List;

public class Composant {
    private int id;
    protected String nom;
    protected TypeComposant typeComposant;
    protected double tauxTVA;
    protected Projet projet;
    private List<Materiau> materiaux = new ArrayList<>();
    private List<MainOeuvre> mainOeuvres = new ArrayList<>();

    public Composant(int id, String nom, TypeComposant typeComposant, double tauxTVA, Projet projet) {
        this.id = id;
        this.nom = nom;
        this.projet = projet;
        this.typeComposant = typeComposant;
        this.tauxTVA = tauxTVA;
    }

    public Composant(String nom, TypeComposant typeComposant, double tauxTVA) {
        this.nom = nom;
        this.typeComposant = typeComposant;
        this.tauxTVA = tauxTVA;
    }

    public Composant() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeComposant getTypeComposant() {
        return typeComposant;
    }

    public void setTypeComposant(TypeComposant typeComposant) {
        this.typeComposant = typeComposant;
    }

    public double getTauxTVA() {
        return tauxTVA;
    }

    public void setTauxTVA(double tauxTVA) {
        this.tauxTVA = tauxTVA;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public List<Materiau> getMateriaux() {
        return materiaux;
    }

    public void setMateriaux(List<Materiau> materiaux) {
        this.materiaux = materiaux;
    }

    public List<MainOeuvre> getMainOeuvres() {
        return mainOeuvres;
    }

    public void setMainOeuvres(List<MainOeuvre> mainOeuvres) {
        this.mainOeuvres = mainOeuvres;
    }

    @Override
    public String toString() {
        return "Composant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", typeComposant=" + typeComposant +
                ", tauxTVA=" + tauxTVA +
                '}';
    }
}
