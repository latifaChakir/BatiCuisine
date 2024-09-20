package ui;

import bean.Projet;

import java.util.Scanner;

public class CalculTotalMenu {
    private Scanner scanner;
    public CalculTotalMenu() {
        this.scanner = new Scanner(System.in);

    }
    public void inputForCalculTotal(Projet projet) {
        System.out.println("Souhaitez vous appliquer une marge benificaire ? (O/N)");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("O")) {
            System.out.println("Veuillez saisir la marge benificaire");
            double marge = scanner.nextDouble();
            scanner.nextLine();
            projet.setMargeBeneficiaire(marge);
        }
    }

}
