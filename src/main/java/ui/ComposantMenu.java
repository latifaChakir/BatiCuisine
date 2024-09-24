package ui;

import bean.*;
import service.ComposantService;
import service.ProjetService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
 public class ComposantMenu {
     private ComposantService composantService;
     private ProjetMenu projetMenu;
     private ProjetService projetService;
     private Scanner scanner;
     private CalculTotalMenu calculTotalMenu;


     public ComposantMenu(ComposantService composantService, Scanner scanner) {
         this.composantService = composantService;
         this.scanner = scanner;
         this.projetService=new ProjetService();
         this.calculTotalMenu = new CalculTotalMenu(scanner);

     }

     public void composantMenu() {
        while (true) {
            System.out.println("1. Ajouter Composant");
            System.out.println("2. Modifier Composant");
            System.out.println("3. Supprimer Composant par id");
            System.out.println("4. Chercher Composant par id");
            System.out.println("5. Afficher tous les composants");
            System.out.println("6. Quitter");
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
//                    addComposant();
                    break;
                case 2:
//                    modifierComposant();
                    break;
                case 3:
                    supprimerComposant();
                    break;
                case 4:
                    chercherComposantParId();
                    break;
                case 5:
                    afficherTousLesComposants();
                    break;
                case 6:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }

    public void ajouterComposantAuProjet(Projet projet) {
        boolean ajouterAutreComposant = true;
        while (ajouterAutreComposant) {
            System.out.println("Ajouter un composant au projet: " + projet.getNomProjet());
            getComposantInput(projet);
            System.out.print("Voulez-vous ajouter un autre composant ? (oui/non): ");
            String reponse = scanner.nextLine();

            if (!reponse.equalsIgnoreCase("oui")) {
                projetService.findById(projet.getId());
                System.out.println("le projet "+projet.getNomProjet()+ " qui a id "+projet.getId()+ " ajouté avec succes ");
                System.out.println("voulez vous calculer le cout  total de projet ? oui/non");
                if (scanner.nextLine().equalsIgnoreCase("oui")){
                    calculTotalProjet();
                }
                    ajouterAutreComposant = false;
            }
        }
    }
    public void updateComposantduProjet(Projet projet) {
        boolean ajouterAutreComposant = true;
        while (ajouterAutreComposant) {
            System.out.println("Modifier un composant du projet: " + projet.getNomProjet());
            getComposantInputToUpdate(projet);

            System.out.print("Voulez-vous modifier un autre composant ? (oui/non): ");
            String reponse = scanner.nextLine();

            if (!reponse.equalsIgnoreCase("oui")) {
                ajouterAutreComposant = false;
            }
        }
    }

    private void supprimerComposant() {
        int composantIdToDelete = getComposantIdInput();
        composantService.delete(composantIdToDelete);
        System.out.println("Composant supprimé avec succès.");
    }

    private void chercherComposantParId() {
        int composantId = getComposantIdInput();
        Composant composantFound = composantService.findById(composantId);
        if (composantFound != null) {
            System.out.println(composantFound);
        } else {
            System.out.println("Composant non trouvé.");
        }
    }

    private void afficherTousLesComposants() {
        List<Composant> composants = composantService.findAll();
        if (!composants.isEmpty()) {
            for (Composant c : composants) {
                System.out.println(c);
            }
        } else {
            System.out.println("Aucun composant trouvé.");
        }
    }

    private void getComposantInput(Projet projet) {
        int type = -1;

        while (type != 1 && type != 2) {
            System.out.print("Type de composant (1: ✨ Materiel, 2: ✨ MainDOeuvre): ");
            String input = scanner.nextLine();

            try {
                type = Integer.parseInt(input);
                if (type != 1 && type != 2) {
                    System.out.println("Veuillez entrer un nombre valide (1 ou 2).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée non valide. Veuillez entrer un nombre valide (1 ou 2).");
            }
        }

        if (type == 1) {
            boolean ajouterAutreMateriau = true;

            while (ajouterAutreMateriau) {
                MateriauMenu materiauMenu = new MateriauMenu(composantService);
                Composant materiau = materiauMenu.getMateriauInput(projet);

                composantService.save(materiau);
                System.out.println("Matériau ajouté avec succès !");

                System.out.print("Voulez-vous ajouter un autre matériau ? (oui/non): ");
                String reponse = scanner.nextLine();

                if (!reponse.equalsIgnoreCase("oui")) {
                    ajouterAutreMateriau = false;
                }
            }

        } else {
            boolean ajouterAutreMainOeuvre = true;

            while (ajouterAutreMainOeuvre) {
                MainOeuvreMenu mainOeuvreMenu = new MainOeuvreMenu(composantService);
                Composant mainOeuvre = mainOeuvreMenu.getMainOeuvreInput(projet);
                composantService.save(mainOeuvre);
                System.out.println("Main d'œuvre ajoutée avec succès !");

                System.out.print("Voulez-vous ajouter une autre main d'œuvre ? (oui/non): ");
                String reponse = scanner.nextLine();

                if (!reponse.equalsIgnoreCase("oui")) {
                    ajouterAutreMainOeuvre = false;
                }
            }
        }
    }
    private void getComposantInputToUpdate(Projet projet) {
        System.out.print("Type de composant (1: Materiel, 2: MainDOeuvre): ");
        int type = Integer.parseInt(scanner.nextLine());

        if (type == 1) {
            boolean ajouterAutreMateriau = true;

            while (ajouterAutreMateriau) {
                MateriauMenu materiauMenu = new MateriauMenu(composantService);
                Composant materiau = materiauMenu.getMateriauInput(projet);
                composantService.save(materiau);
                System.out.println("Matériau ajouté avec succès !");

                System.out.print("Voulez-vous ajouter un autre matériau ? (oui/non): ");
                String reponse = scanner.nextLine();

                if (!reponse.equalsIgnoreCase("oui")) {
                    ajouterAutreMateriau = false;
                }
            }

        } else if (type == 2) {
            boolean ajouterAutreMainOeuvre = true;

            while (ajouterAutreMainOeuvre) {
                MainOeuvreMenu mainOeuvreMenu = new MainOeuvreMenu(composantService);
                Composant mainOeuvre = mainOeuvreMenu.getMainOeuvreInput(projet);

                composantService.save(mainOeuvre);
                System.out.println("Main d'œuvre ajoutée avec succès !");

                System.out.print("Voulez-vous ajouter une autre main d'œuvre ? (oui/non): ");
                String reponse = scanner.nextLine();

                if (!reponse.equalsIgnoreCase("oui")) {
                    ajouterAutreMainOeuvre = false;
                }
            }
        } else {
            System.out.println("Option invalide. Veuillez entrer 1 ou 2.");
        }
    }

    private int getComposantIdInput() {
        System.out.print("Entrer l'ID du composant: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void addComposant(){
        System.out.println("Entrer l'ID du projet: ");
        int idProjet = Integer.parseInt(scanner.nextLine());
        Optional<Projet> projetOpt = projetService.findById(idProjet);

        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();
            ajouterComposantAuProjet(projet);
        } else {
            System.out.println("Projet avec l'ID " + idProjet + " non trouvé.");
        }
    }

     public void calculTotalProjet() {
         System.out.print("Entrez l'ID du projet que vous souhaitez calculer son total  :");
         int projectId = Integer.parseInt(scanner.nextLine());

         Optional<Projet> projetOptional = projetService.findById(projectId);
         if (projetOptional.isPresent()) {
             Projet projet = projetOptional.get();
             System.out.println("--- Détail du Projet ---");
             System.out.println("✦ ID: " + projet.getId());
             System.out.println("✦ Nom de projet: " + projet.getNomProjet());
             System.out.println("✦ Surface: " + projet.getSurface());
             System.out.println("✦ État de projet: " + projet.getEtat());
             System.out.println("---- Détails du Client ----");
             Client client = projet.getClient();
             if (client != null) {
                 System.out.println("✦ ID: " + client.getId());
                 System.out.println("✦ Nom: " + client.getNom());
                 System.out.println("✦ Adresse: " + client.getAdresse());
                 System.out.println("✦ Téléphone: " + client.getTelephone());
                 System.out.println("---- Détails des Composants du Projet ----");

                 double totalCoutMateriaux = 0.0;
                 double totalCoutMateriauxAvecTVA = 0.0;
                 double totalCoutMainOeuvre = 0.0;
                 double totalCoutMainOeuvreAvecTVA = 0.0;

                 boolean hasMaterials = false;
                 boolean hasMainOeuvre = false;

                 // Traitement des matériaux
                 for (Composant composant : projet.getComposants()) {
                     if (composant.getMateriaux() != null && !composant.getMateriaux().isEmpty()) {
                         if (!hasMaterials) {
                             System.out.println("1. Matériaux :");
                             hasMaterials = true;
                         }
                         for (Materiau materiau : composant.getMateriaux()) {
                             double coutMateriauBrute = materiau.getQuantite() * materiau.getCoutUnitaire();
                             double coutMateriauAjuste = coutMateriauBrute * materiau.getCoefficientQualite();
                             double coutMateriauTotal = coutMateriauAjuste + materiau.getCoutTransport();
                             totalCoutMateriaux += coutMateriauTotal;

                             double tva = composant.getTauxTVA() / 100;
                             double coutMateriauTotalAvecTVA = coutMateriauTotal * (1 + tva);
                             totalCoutMateriauxAvecTVA += coutMateriauTotalAvecTVA;

                             System.out.printf("- %s : %.2f DH (quantité : %.2f, coût unitaire : %.2f DH/unité, qualité : %.2f, transport : %.2f DH, total avec TVA : %.2f DH)\n",
                                     composant.getNom(),
                                     coutMateriauTotal,
                                     materiau.getQuantite(),
                                     materiau.getCoutUnitaire(),
                                     materiau.getCoefficientQualite(),
                                     materiau.getCoutTransport(),
                                     coutMateriauTotalAvecTVA);
                         }
                     }
                 }

                 if (hasMaterials) {
                     System.out.printf("\uD83D\uDCB0 Coût total des matériaux avant TVA : %.2f DH\n", totalCoutMateriaux);
                     System.out.printf("\uD83D\uDCB0 Coût total des matériaux avec TVA  : %.2f DH\n", totalCoutMateriauxAvecTVA);
                 }
                 // Traitement de la main-d'œuvre
                 for (Composant composant : projet.getComposants()) {
                     if (composant.getMainOeuvres() != null && !composant.getMainOeuvres().isEmpty()) {
                         if (!hasMainOeuvre) {
                             System.out.println("2. Main-d'œuvre :");
                             hasMainOeuvre = true;
                         }
                         for (MainOeuvre mainOeuvre : composant.getMainOeuvres()) {
                             double coutMainOeuvre = mainOeuvre.getTauxHoraire() * mainOeuvre.getHeuresTravail();
                             totalCoutMainOeuvre += coutMainOeuvre * mainOeuvre.getProductiviteOuvrier();

                             double tva = composant.getTauxTVA() / 100;
                             double coutMainOeuvreAvecTVA = coutMainOeuvre * (1 + tva);
                             totalCoutMainOeuvreAvecTVA += coutMainOeuvreAvecTVA;

                             System.out.printf("- %s : %.2f DH (taux horaire : %.2f DH/h, heures travaillées : %.2f h, productivité : %.1f, total avec TVA : %.2f DH)\n",
                                     composant.getNom(),
                                     coutMainOeuvre,
                                     mainOeuvre.getTauxHoraire(),
                                     mainOeuvre.getHeuresTravail(),
                                     mainOeuvre.getProductiviteOuvrier(),
                                     coutMainOeuvreAvecTVA);
                         }
                     }
                 }

                 if (hasMainOeuvre) {
                     System.out.printf("\uD83D\uDCB0 Coût total de la main-d'œuvre avant TVA : %.2f DH \uD83D\uDCB0\n", totalCoutMainOeuvre);
                     System.out.printf("\uD83D\uDCB0 Coût total de la main-d'œuvre avec TVA : %.2f DH \uD83D\uDCB0\n", totalCoutMainOeuvreAvecTVA);
                 }

                 System.out.println("--- Calcul en cours \uD83D\uDD22 ---\n");
                 calculTotalMenu.inputForCalculTotal(projet);
                 double coutTotal = totalCoutMainOeuvre+totalCoutMateriaux;
                 double coutTotalAvantMarge = totalCoutMainOeuvreAvecTVA+totalCoutMateriauxAvecTVA;
                 double coutTotalAvecMarge = coutTotalAvantMarge * (1 + (projet.getMargeBeneficiaire()/100));

                 System.out.println("- total de projet avant marge : "+coutTotalAvantMarge);
                 System.out.println("- Marge beneficaire : "+projet.getMargeBeneficiaire());
                 System.out.println("- total de projet final : " + coutTotalAvecMarge);
                 projet.setCoutTotal(coutTotalAvecMarge);
                 projet.setMargeBeneficiaire(projet.getMargeBeneficiaire());
                 projetService.miseAJourProjet(projet);

             }
         } else {
             System.out.println("Projet non trouvé.");
         }
     }



 }
