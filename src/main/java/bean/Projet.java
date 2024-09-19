package bean;

import bean.enums.EtatProjet;

import java.util.List;

public class Projet {
    private int id;
    private String nomProjet;
    private double margeBeneficiaire;
    private double coutTotal;
    private EtatProjet etat;
    private double surface;
    private Client client;
    List<Composant> composants;

    public Projet(int id, String nomProjet, double margeBeneficiaire, double coutTotal, EtatProjet etat, Client client, double surface) {
        this.id = id;
        this.nomProjet = nomProjet;
        this.surface = surface;
        this.margeBeneficiaire = margeBeneficiaire;
        this.coutTotal = coutTotal;
        this.etat = etat;
        this.client = client;
    }

    public Projet(String nomProjet, double margeBeneficiaire, double coutTotal, EtatProjet etat, Client client,double surface) {
        this.nomProjet = nomProjet;
        this.surface = surface;
        this.margeBeneficiaire = margeBeneficiaire;
        this.coutTotal = coutTotal;
        this.etat = etat;
        this.client = client;
    }

    public Projet() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public double getMargeBeneficiaire() {
        return margeBeneficiaire;
    }

    public void setMargeBeneficiaire(double margeBeneficiaire) {
        this.margeBeneficiaire = margeBeneficiaire;
    }

    public double getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(double coutTotal) {
        this.coutTotal = coutTotal;
    }

    public EtatProjet getEtat() {
        return etat;
    }

    public void setEtat(EtatProjet etat) {
        this.etat = etat;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Composant> getComposants() {
        return composants;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public void setComposants(List<Composant> composants) {
        this.composants = composants;
    }

    public void ajouterComposant(Composant composant) {
        composants.add(composant);
    }
    @Override
    public String toString() {
        return "Projet{" +
                "id=" + id +
                ", nomProjet='" + nomProjet + '\'' +
                ", margeBeneficiaire=" + margeBeneficiaire +
                ", coutTotal=" + coutTotal +
                ", surface=" + surface +
                ", etat=" + etat +
                ", client=" + client +
                '}';
    }
}
