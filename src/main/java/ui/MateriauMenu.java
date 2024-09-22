package ui;

import bean.Composant;
import bean.Materiau;
import bean.Projet;
import bean.enums.TypeComposant;
import exceptions.DevisValidationException;
import exceptions.MateriauValidationException;
import service.ComposantService;
import utils.Validations;

import java.util.List;
import java.util.Scanner;

public class MateriauMenu {
    private ComposantService composantService;
    private Scanner scanner;

    public MateriauMenu(ComposantService composantService) {
        this.composantService = composantService;
        this.scanner = new Scanner(System.in);
    }

    public Materiau getMateriauInput(Projet projet) {
        System.out.print("Entrer le nom du matériau: ");
        String nom = scanner.nextLine();
        System.out.print("Entrer le taux de TVA: ");
        double tauxTVA = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le coût unitaire: ");
        double coutUnitaire = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer la quantité: ");
        double quantite = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le coût de transport: ");
        double coutTransport = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le coefficient de qualité: ");
        double coefficientQualite = Double.parseDouble(scanner.nextLine());

        Materiau materiau=new Materiau(0, nom, TypeComposant.Materiel, tauxTVA, coutUnitaire, quantite, coutTransport, coefficientQualite, projet);
        try {
            Validations.materiauValidation(materiau);
        } catch (MateriauValidationException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return materiau;
    }

}
