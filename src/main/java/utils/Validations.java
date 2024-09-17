package utils;

import bean.Client;
import exceptions.ClientValidationException;

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
}
