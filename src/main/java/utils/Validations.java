package utils;

import bean.Client;
import bean.Devis;
import bean.Projet;
import exceptions.ClientValidationException;
import exceptions.DevisValidationException;
import exceptions.ProjectValidationException;
import service.ProjetService;

public class Validations {
    public static void clientValidation(Client client) {
        if (client.getNom() == null || client.getNom().isEmpty()) {
            throw new ClientValidationException("Le nom du client ne peut pas être vide.");
        }
        if (client.getAdresse() == null || client.getAdresse().isEmpty()) {
            throw new ClientValidationException("L'adresse du client ne peut pas être vide.");
        }
        if (client.getTelephone() == null || client.getTelephone().isEmpty()) {
            throw new ClientValidationException("Le téléphone du client ne peut pas être vide.");
        }
        if (!client.getTelephone().matches("\\d{10}")) {
            throw new ClientValidationException("Le téléphone du client doit comporter 10 chiffres.");
        }
    }
    public static void projetValidation(Projet projet) {
        if(projet.getNomProjet() == null || projet.getNomProjet().isEmpty()) {
            throw new ProjectValidationException("Le nom du projet ne peut pas être vide.");
        }
        if(projet.getSurface() <= 0) {
            throw new ProjectValidationException("La surface du projet ne peut pas être inférieure ou égale à 0.");
        }
        if(projet.getClient() == null || projet.getClient().getId() <=0) {
            throw new ProjectValidationException("Le client du projet ne peut pas être nul ou son id inférieur ou égal à 0.");
        }
        if(projet.getMargeBeneficiaire() < 0) {
            throw new ProjectValidationException("La marge du projet ne peut pas être inférieure ou égale à 0.");
        }
        if(projet.getEtat() == null) {
            throw new ProjectValidationException("L'état du projet ne peut pas être nul.");
        }
        if(projet.getCoutTotal() <0){
            throw new ProjectValidationException("le total de projet ne peut pas etre inférieure à 0");
        }
    }
    public static void devisValidation(Devis devis) {
        if(devis.getProjet() == null || devis.getProjet().getId() <= 0){
            throw new DevisValidationException("Le projet du devis ne peut pas être nul ou son id inférieur ou égal à 0.");
        }
        if(devis.getEstimatedAmount() <= 0) {
            throw new DevisValidationException("Le montant estimé du devis ne peut pas être inférieur ou égal à 0.");
        }
        if(devis.getIssueDate() == null) {
            throw new DevisValidationException("La date d'émission du devis ne peut pas être nulle.");
        }
        if(devis.getValidatedDate()==null && devis.getValidatedDate().isBefore(devis.getIssueDate())) {
            throw new DevisValidationException("La date de validation du devis ne peut pas être antérieure à la date d'émission.");
        }

    }
}
