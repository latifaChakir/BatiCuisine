package bean;

import bean.enums.TypeComposant;

public class MainOeuvre extends Composant {
    private double tauxHoraire;
    private double heuresTravail;
    private double productiviteOuvrier;
    private Composant composant;

    public MainOeuvre(int id, String nom, TypeComposant typeComposant,Projet projet, double tauxTVA, double tauxHoraire, double heuresTravail, double productiviteOuvrier) {
        super(id, nom, typeComposant, tauxTVA,projet);
        this.tauxHoraire = tauxHoraire;
        this.heuresTravail = heuresTravail;
        this.productiviteOuvrier = productiviteOuvrier;
    }

    public MainOeuvre(String nom, TypeComposant typeComposant, double tauxTVA, double tauxHoraire, double heuresTravail, double productiviteOuvrier) {
        super(nom, typeComposant, tauxTVA);
        this.tauxHoraire = tauxHoraire;
        this.heuresTravail = heuresTravail;
        this.productiviteOuvrier = productiviteOuvrier;
    }

    public MainOeuvre() {}

    public double getTauxHoraire() {
        return tauxHoraire;
    }

    public void setTauxHoraire(double tauxHoraire) {
        this.tauxHoraire = tauxHoraire;
    }

    public double getHeuresTravail() {
        return heuresTravail;
    }

    public void setHeuresTravail(double heuresTravail) {
        this.heuresTravail = heuresTravail;
    }

    public double getProductiviteOuvrier() {
        return productiviteOuvrier;
    }

    public void setProductiviteOuvrier(double productiviteOuvrier) {
        this.productiviteOuvrier = productiviteOuvrier;
    }

    public double calculerCoutTotal() {
        double coutTotal = (tauxHoraire * heuresTravail) * productiviteOuvrier;
        return coutTotal * (1 + getTauxTVA() / 100);
    }

    public Composant getComposant() {
        return composant;
    }

    public void setComposant(Composant composant) {
        this.composant = composant;
    }

    @Override
    public String toString() {
        return "MainOeuvre{" +
                "tauxHoraire=" + tauxHoraire +
                ", heuresTravail=" + heuresTravail +
                ", productiviteOuvrier=" + productiviteOuvrier +
                "} " + super.toString();
    }
}
