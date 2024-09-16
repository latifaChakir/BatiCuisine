package bean;

import bean.enums.TypeComposant;

public class Materiau extends Composant {
    private double coutUnitaire;
    private double quantite;
    private double coutTransport;
    private double coefficientQualite;

    public Materiau(int id, String nom, TypeComposant typeComposant, double tauxTVA, double coutUnitaire, double quantite, double coutTransport, double coefficientQualite,Projet projet) {
        super(id, nom, typeComposant, tauxTVA,projet);
        this.coutUnitaire = coutUnitaire;
        this.quantite = quantite;
        this.coutTransport = coutTransport;
        this.coefficientQualite = coefficientQualite;
    }

    public Materiau(String nom, TypeComposant typeComposant, double tauxTVA, double coutUnitaire, double quantite, double coutTransport, double coefficientQualite) {
        super(nom, typeComposant, tauxTVA);
        this.coutUnitaire = coutUnitaire;
        this.quantite = quantite;
        this.coutTransport = coutTransport;
        this.coefficientQualite = coefficientQualite;
    }

    public Materiau() {}

    // Getters and Setters
    public double getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(double coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getCoutTransport() {
        return coutTransport;
    }

    public void setCoutTransport(double coutTransport) {
        this.coutTransport = coutTransport;
    }

    public double getCoefficientQualite() {
        return coefficientQualite;
    }

    public void setCoefficientQualite(double coefficientQualite) {
        this.coefficientQualite = coefficientQualite;
    }

    public double calculerCoutTotal() {
        double coutTotal = (coutUnitaire * quantite) + coutTransport;
        return coutTotal * (1 + tauxTVA / 100);
    }

    @Override
    public String toString() {
        return "Materiau{" +
                "coutUnitaire=" + coutUnitaire +
                ", quantite=" + quantite +
                ", coutTransport=" + coutTransport +
                ", coefficientQualite=" + coefficientQualite +
                "} " + super.toString();
    }
}
