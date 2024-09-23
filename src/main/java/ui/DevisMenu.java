package ui;

import bean.Client;
import bean.Composant;
import bean.Devis;
import bean.Projet;
import bean.enums.EtatProjet;
import exceptions.ClientValidationException;
import exceptions.DevisValidationException;
import service.DevisService;
import service.ProjetService;
import utils.Validations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DevisMenu {
    private DevisService devisService;
    private Scanner scanner;
    private ProjetService projetService;

    public DevisMenu(DevisService devisService, Scanner scanner) {
        this.devisService = devisService;
        this.projetService=new ProjetService();
        this.scanner = scanner;
    }
    public void devisMenu() {
        while (true) {
            System.out.println("1. ➤ Ajouter Devis");
            System.out.println("2. ➤ Modifier Devis");
            System.out.println("3. ➤ Supprimer Devis par id");
            System.out.println("4. ➤ Chercher Devis par id");
            System.out.println("5. ➤ Afficher tous les Deviss");
            System.out.println("6. ➤ Accepter Devis");
            System.out.println("7. ➤ Quitter");
            System.out.print("Choisir une option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                continue;
            }

            switch (choice) {
                case 1:
                     Devis C=getDevisInput();
                    if (C != null) {
                        devisService.save(C);
                    } else {
                        System.err.println("Le devis n'a pas été ajouté en raison d'erreurs de validation.");
                    }
                    break;
                case 2:
                   updateDevis();

                    break;
                case 3:
                    int DevisIdToDelete = getDevisIdInput();
                    devisService.delete(DevisIdToDelete);
                    System.out.println("Devis supprimé avec succès.");
                    break;
                case 4:
                    findDevisById();
                    break;

                case 5:findAll();

                    break;
                case 6:accepteDevis();
                    break;
                case 7:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }

    private int getDevisIdInput() {
        System.out.print("Entrer l'ID du devis: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private Devis getDevisInput(){
        System.out.print("Entrer l'ID du projet: ");
        int projetId=scanner.nextInt();
        Projet projet=new Projet();
        projet.setId(projetId);
        scanner.nextLine();
        Optional<Projet> projet1=projetService.findById(projetId);
        if (!projet1.isPresent()) {
            System.err.println("Projet non trouvé avec l'ID : " + projetId);
            return null;
        }
        double montantEstimation=projet1.get().getCoutTotal();
        LocalDate estimationDate = null;
        while (estimationDate == null) {
            System.out.print("Entrer la date d'émission (format AAAA-MM-JJ) : ");
            String dateInput = scanner.nextLine();
            try {
                estimationDate = LocalDate.parse(dateInput);
                if (estimationDate.isAfter(LocalDate.now())) {
                    System.err.println("La date de validation ne peut pas être antérieure à la date d'émission.");
                    estimationDate = null;
                }
            } catch (DateTimeParseException e) {
                System.err.println("Date invalide. Veuillez entrer une date valide au format AAAA-MM-JJ.");
            }
        }
        LocalDate dateValidation = null;
        while (dateValidation == null) {
            System.out.print("Entrer la date de validation (format AAAA-MM-JJ) : ");
            String validationInput = scanner.nextLine();
            try {
                dateValidation = LocalDate.parse(validationInput);
                if (dateValidation.isBefore(estimationDate)) {
                    System.err.println("La date de validation ne peut pas être antérieure à la date d'émission.");
                    dateValidation = null;
                }
            } catch (DateTimeParseException e) {
                System.err.println("Date invalide. Veuillez entrer une date valide au format AAAA-MM-JJ.");
            }
        }

        Devis devis=new Devis(0L, montantEstimation,estimationDate,dateValidation,false,projet);
        try {
            Validations.devisValidation(devis);
        } catch (DevisValidationException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return devis;
    }

    public  void findAll() {
        int boxWidth = 80;
        String borderTop = "╔" + "═".repeat(boxWidth - 2) + "╗";
        String borderBottom = "╚" + "═".repeat(boxWidth - 2) + "╝";
        List<Devis> Deviss = devisService.findAll();
        if (!Deviss.isEmpty()) {
            Deviss.stream()
                    .sorted(Comparator.comparing(Devis::getIssueDate))
                    .forEach(devis -> {
                Projet projet = devis.getProjet();
                Client client = projet.getClient();
                List<Composant> composants = projet.getComposants();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        System.out.println(borderTop);
                        System.out.println("║ Devis n°" + devis.getId() + " - Projet " + projet.getNomProjet()+ " ".repeat(46 ));
                        System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");
                        System.out.println("║");
                        System.out.println("║ Date d'émission: " + devis.getIssueDate().format(formatter));
                        System.out.println("║ Date de validité: " + devis.getValidatedDate().format(formatter));
                        System.out.println("║");
                        System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");
                        System.out.println("║ ✦ Détails du client");
                        System.out.println("║ Nom: " + client.getNom());
                        System.out.println("║ Adresse: " + client.getAdresse());
                        System.out.println("║ Téléphone: " + client.getTelephone());
                        System.out.println("║ Type de client: " + (client.isEstProfessionnel() ? "Professionnel" : "Particulier"));
                        System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");
                        System.out.println("║");
                        System.out.println("║ ✦ Description du projet");
                        System.out.println("║ Nom du projet: " + projet.getNomProjet());
                        System.out.println("║ État du projet: " + projet.getEtat());
                        System.out.println("║");

                        System.out.println("║ ✦ Estimation détaillée");
                        System.out.println("║");
                        System.out.println("║ ✦ Composants");
                        for (Composant composant : composants) {
                            System.out.println("║ - " + composant.getNom() + " (" + composant.getTypeComposant() + ")");
                            System.out.println("║  TVA: " + composant.getTauxTVA() + "%");
                        }
                        System.out.println("║");
                        System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");

                        System.out.println("║ ✦ Coûts");
                        System.out.printf("║ Coût total du projet: %.2f €%n", projet.getCoutTotal());
                        System.out.println("║");

                        double marge = projet.getCoutTotal() * (projet.getMargeBeneficiaire() / 100);
                        System.out.println("║ ✦ Marge bénéficiaire");
                        System.out.println("║ Taux de marge: " + projet.getMargeBeneficiaire() + "%");
                        System.out.printf("║ Montant de la marge: %.2f €%n", marge);
                        System.out.println("║");

                        System.out.println("║ ✦ Récapitulatif");
                        System.out.printf("║ Coût total (hors marge): %.2f €%n", projet.getCoutTotal());
                        System.out.printf("║ Montant total estimé: %.2f €%n", devis.getEstimatedAmount());
                        System.out.println("║");

                        System.out.println("║ ✦ Acceptation du Devis");
                        System.out.println("║ " + (devis.isAccepted() ? "☑ Accepté   □ Refusé" : "□ Accepté   ☑ Refusé"));
                        System.out.println("║ Date d'acceptation: " + devis.getValidatedDate().format(formatter));
                        System.out.println("║");
                        System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");
                        System.out.println("║ ✦ Informations complémentaires:**");
                        System.out.println("║ - ID du Devis: " + devis.getId());
                        System.out.println("║ - ID du projet: " + projet.getId());
                        System.out.println("║ - ID du client: " + client.getId());
                        System.out.println("║");
                        System.out.println(borderBottom);
            });


        } else {
            System.out.println("Aucun Devis trouvé.");
        }
    }

    public void findDevisById() {
        int boxWidth = 80;
        String borderTop = "╔" + "═".repeat(boxWidth - 2) + "╗";
        String borderBottom = "╚" + "═".repeat(boxWidth - 2) + "╝";
        int DevisId = getDevisIdInput();
        Devis DevisFound = devisService.findById(DevisId);

        if (DevisFound != null) {
            Projet projet = DevisFound.getProjet();
            Client client = projet.getClient();
            List<Composant> composants = projet.getComposants();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println(borderTop);
            System.out.println("║ Devis n°" + DevisFound.getId() + " - Projet " + projet.getNomProjet()+ " ".repeat(46 )+"╣");
            System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");
            System.out.println("║");
            System.out.println("║ Date d'émission:" + DevisFound.getIssueDate().format(formatter));
            System.out.println("║ Date de validité: " + DevisFound.getValidatedDate().format(formatter));
            System.out.println("║");
            System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");
            System.out.println("║ ✦ Détails du client");
            System.out.println("║ Nom: " + client.getNom());
            System.out.println("║ Adresse: " + client.getAdresse());
            System.out.println("║ Téléphone: " + client.getTelephone());
            System.out.println("║ Type de client: " + (client.isEstProfessionnel() ? "Professionnel" : "Particulier"));
            System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");
            System.out.println("║");
            System.out.println("║ ✦ Description du projet");
            System.out.println("║ Nom du projet: " + projet.getNomProjet());
            System.out.println("║ État du projet: " + projet.getEtat());
            System.out.println("║");

            System.out.println("║ ✦ Estimation détaillée");
            System.out.println("║");
            System.out.println("║ ✦ Composants");
            for (Composant composant : composants) {
                System.out.println("║ - " + composant.getNom() + " (" + composant.getTypeComposant() + ")");
                System.out.println("║  TVA: " + composant.getTauxTVA() + "%");
            }
            System.out.println("║");
            System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");

            System.out.println("║ ✦ Coûts");
            System.out.printf("║ Coût total du projet: %.2f €%n", projet.getCoutTotal());
            System.out.println("║");

            double marge = projet.getCoutTotal() * (projet.getMargeBeneficiaire() / 100);
            System.out.println("║ ✦ Marge bénéficiaire");
            System.out.println("║ Taux de marge: " + projet.getMargeBeneficiaire() + "%");
            System.out.printf("║ Montant de la marge: %.2f €%n", marge);
            System.out.println("║");

            System.out.println("║ ✦ Récapitulatif");
            System.out.printf("║ Coût total (hors marge): %.2f €%n", projet.getCoutTotal());
            System.out.printf("║ Montant total estimé: %.2f €%n", DevisFound.getEstimatedAmount());
            System.out.println("║");

            System.out.println("║ ✦ Acceptation du Devis");
            System.out.println("║ " + (DevisFound.isAccepted() ? "☑ Accepté   □ Refusé" : "□ Accepté   ☑ Refusé"));
            System.out.println("║ Date d'acceptation: " + DevisFound.getValidatedDate().format(formatter));
            System.out.println("║");
            System.out.println("║" + "═".repeat(boxWidth - 2) + "╣");
            System.out.println("║ ✦ Informations complémentaires: ");
            System.out.println("║ - ID du Devis: " + DevisFound.getId());
            System.out.println("║ - ID du projet: " + projet.getId());
            System.out.println("║ - ID du client: " + client.getId());
            System.out.println("║");
            System.out.println(borderBottom);
        } else {
            System.out.println("Devis non trouvé.");
        }
    }

    private void updateDevis() {
        System.out.print("Entrer l'ID du devis à modifier: ");
        int devisId = Integer.parseInt(scanner.nextLine());
        Devis devis = devisService.findById(devisId);

        if (devis != null) {
            System.out.print("Entrer un nouveau montant d'estimation (actuel: " + devis.getEstimatedAmount() + "): ");
            String newAmountInput = scanner.nextLine();
            if (!newAmountInput.trim().isEmpty()) {
                double newAmount = Double.parseDouble(newAmountInput);
                devis.setEstimatedAmount(newAmount);
            }

            System.out.print("Entrer une nouvelle date d'émission (actuelle: " + devis.getIssueDate() + "): ");
            String newIssueDateInput = scanner.nextLine();
            if (!newIssueDateInput.trim().isEmpty()) {
                LocalDate newIssueDate = LocalDate.parse(newIssueDateInput);
                devis.setIssueDate(newIssueDate);
            }

            System.out.print("Entrer une nouvelle date de validation (actuelle: " + devis.getValidatedDate() + "): ");
            String newValidatedDateInput = scanner.nextLine();
            if (!newValidatedDateInput.trim().isEmpty()) {
                LocalDate newValidatedDate = LocalDate.parse(newValidatedDateInput);
                devis.setValidatedDate(newValidatedDate);
            }

            try {
                Validations.devisValidation(devis);
                System.out.print("le devis est accepté OUI/NON ?");
                String reponse=scanner.nextLine();
                if(reponse.equals("OUI")){
                    devis.setAccepted(true);
                }
                devisService.update(devis);
                System.out.println("Devis modifié avec succès.");
            } catch (DevisValidationException e) {
                System.err.println(e.getMessage());
            }

        } else {
            System.out.println("Devis non trouvé.");
        }
    }


    public void accepteDevis() {
        int DevisId = getDevisIdInput();
        Devis devisFound = devisService.findById(DevisId);

        if (devisFound != null) {
            System.out.println("Voulez-vous accepter le devis ? (Oui/Non)");
            String reponse = scanner.nextLine();
            Optional<Projet> projetOpt = projetService.findById(devisFound.getProjet().getId());
            if (!projetOpt.isPresent()) {
                System.out.println("Projet associé non trouvé.");
                return;
            }

            Projet projet = projetOpt.get();

            if (reponse.equalsIgnoreCase("Oui")) {
                devisFound.setAccepted(true);
                projet.setEtat(EtatProjet.TERMINE);
                devisFound.setValidatedDate(LocalDate.now());
                System.out.println("Devis accepté avec succès.");
                devisService.update(devisFound);
                projetService.updateEtatProjet(projet,projet.getEtat().name());

            } else if (reponse.equalsIgnoreCase("Non")) {
                devisFound.setAccepted(false);
                projet.setEtat(EtatProjet.ANNULE);
                devisFound.setValidatedDate(LocalDate.now());
                System.out.println("Devis rejeté avec succès.");
                devisService.update(devisFound);
                projetService.updateEtatProjet(projet,projet.getEtat().name());

            } else {
                System.out.println("Réponse invalide. Veuillez entrer 'Oui' ou 'Non'.");
                return;
            }

        } else {
            System.out.println("Devis non trouvé.");
        }
    }

}
