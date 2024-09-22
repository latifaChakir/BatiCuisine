package ui;

import bean.MainOeuvre;
import bean.Projet;
import bean.enums.TypeComposant;
import exceptions.MainOeuvreValidationException;
import service.ComposantService;
import utils.Validations;

import java.util.Scanner;

public class MainOeuvreMenu {
    private ComposantService composantService;
    private Scanner scanner;

    public MainOeuvreMenu(ComposantService composantService) {
        this.composantService = composantService;
        this.scanner = new Scanner(System.in);
    }


    public MainOeuvre getMainOeuvreInput(Projet projet) {
        System.out.print("Entrer le nom de l'ouvrier: ");
        String nom = scanner.nextLine();
        System.out.print("Entrer le taux de TVA: ");
        double tauxTVA = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le taux horaire: ");
        double tauxHoraire = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer le nombre d'heures travaillées: ");
        double heuresTravail = Double.parseDouble(scanner.nextLine());
        System.out.print("Entrer la productivité de l'ouvrier: ");
        double productiviteOuvrier = Double.parseDouble(scanner.nextLine());
        MainOeuvre mainOeuvre=new MainOeuvre(0, nom, TypeComposant.MainDOeuvre, projet, tauxTVA, tauxHoraire, heuresTravail, productiviteOuvrier);
        try {
            Validations.mainOeuvreValidation(mainOeuvre);
        } catch (MainOeuvreValidationException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return mainOeuvre;    }
}
