package ui;

import bean.Client;
import bean.Composant;
import bean.Projet;
import bean.enums.EtatProjet;
import service.ClientService;
import service.ComposantService;
import service.ProjetService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjetMenu {
    private ComposantService composantService;
    private ProjetService projetService;
    private ClientMenu clientMenu;
    private static Scanner scanner;

    public ProjetMenu(ProjetService projetService) {
        this.projetService = projetService;
        this.composantService=new ComposantService();
        this.clientMenu = new ClientMenu(new ClientService());
        scanner = new Scanner(System.in);
    }


    private Projet inputsProjet() {
        Client client = null;

        while (client == null) {
            System.out.println("Voulez-vous associer ce projet à un client existant ou créer un nouveau client ?");
            System.out.println("1. Chercher un client existant");
            System.out.println("2. Ajouter un nouveau client");
            System.out.print("Choisir une option: ");

            int choixClient = Integer.parseInt(scanner.nextLine());

            switch (choixClient) {
                case 1:
                    client = searchClientForProject();
                    break;
                case 2:
                    client = addNewClientForProject();
                    break;
                default:
                    System.out.println("Option non valide. Veuillez réessayer.");
            }
        }

        System.out.print("Nom du projet : ");
        String nomProjet = scanner.nextLine();
        System.out.print("Surface : ");
        double surface = Double.parseDouble(scanner.nextLine());

        System.out.print("État du projet (par ex: ENCOURS, TERMINE, ANNULE) : ");
        String etatInput = scanner.nextLine().toUpperCase();
        EtatProjet etat = EtatProjet.valueOf(etatInput);

        return new Projet(nomProjet, 0, 0, etat, client, surface);
    }

    private Client searchClientForProject() {
        System.out.print("Entrer le nom du client : ");
        String name = scanner.nextLine();
        List<Client> clientsByName = clientMenu.findClientByName(name);

        if (!clientsByName.isEmpty()) {
            System.out.print("Sélectionnez un client par ID ou tapez '0' pour annuler.");

            int clientId = Integer.parseInt(scanner.nextLine());
            if (clientId == 0) {
                System.out.println("Opération annulée.");
                return null;
            }

            for (Client client : clientsByName) {
                if (client.getId() == clientId) {
                    return client;
                }
            }

            System.out.println("Client avec l'ID " + clientId + " non trouvé dans la liste.");
        } else {
            System.out.println("Client non trouvé.");
        }

        return null;
    }

    private void handleSaveProject(Projet projet, Client client) {
        if (client.getId() == 0) {
            projetService.saveProjetClient(projet, client);
        } else {
            projetService.saveProjetClientFound(projet, client);
        }
    }

    private Client addNewClientForProject() {
        return clientMenu.addNewClient();
    }

    private void deleteProjetById() {
        System.out.print("Entrer l'ID du projet à supprimer : ");
        int projectId = Integer.parseInt(scanner.nextLine());
        projetService.delete(projectId);
        System.out.println("Projet supprimé.");
    }

    private void findProjetById() {
        System.out.print("Entrer l'ID du projet : ");
        int projectId = Integer.parseInt(scanner.nextLine());
        Optional<Projet> projet = projetService.findById(projectId);
        if (projet.isPresent()) {
            System.out.println(projet.get());
        } else {
            System.out.println("Projet non trouvé.");
        }
    }


    private int getProjetIdInput() {
        System.out.print("Entrer l'ID du projet : ");
        return Integer.parseInt(scanner.nextLine());
    }

    public void createProject() {
        Projet projet = inputsProjet();
        handleSaveProject(projet, projet.getClient());
        System.out.println("Voulez-vous ajouter des composants à ce projet ? (oui/non)");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("oui")) {
            ComposantMenu composantMenu = new ComposantMenu(new ComposantService());
            composantMenu.ajouterComposantAuProjet(projet);
        }
    }
    private List<Composant> findByProject(Projet projet){
        List<Composant> composants = composantService.findByProject(projet);
        if (!composants.isEmpty()) {
//            for (Composant c : composants) {
//                System.out.println(c);
//            }
        } else {
            System.out.println("Aucun composant trouvé.");
        }
        return composants;
    }

    public void updateProject() {
        System.out.print("Entrer l'ID du projet à mettre à jour : ");
        int projectId = Integer.parseInt(scanner.nextLine());

        Optional<Projet> projetOptional = projetService.findById(projectId);
        if (projetOptional.isPresent()) {
            Projet projet = projetOptional.get();
            List<Composant> composants = findByProject(projet);
            boolean materiauExisteDeja = false;

            for (Composant c : composants) {
                materiauExisteDeja = true;
                break;
            }

            if (materiauExisteDeja) {
                composantService.supprimerComposantsParProjet(projet);
            }
            System.out.println("Projet trouvé : " + projet);

            Projet updatedProjet = inputsProjet();
            updatedProjet.setId(projectId);

            projetService.update(updatedProjet);

            System.out.println("Voulez-vous modifier les composants de ce projet ? (oui/non)");
            String reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("oui")) {
                ComposantMenu composantMenu = new ComposantMenu(new ComposantService());
                composantMenu.updateComposantduProjet(updatedProjet);
            }

            System.out.println("Projet et composants mis à jour avec succès.");
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

    public void findAllProject(){
        projetService.findAll().forEach(projet ->{
            System.out.print("les détails de projet");
            System.out.print("ID: " + projet.getId());
            System.out.print("Nom de projet: " + projet.getNomProjet());
            System.out.print("Surface: " + projet.getSurface());
            System.out.print("Etat de projet: " + projet.getEtat());
            System.out.print("Marge beneficiaire: " + projet.getMargeBeneficiaire());
            System.out.print("Cout Total: " + projet.getCoutTotal());

            System.out.println("les détails de client de ce projet");
            Client client=projet.getClient();
            if(client!=null){
                System.out.print("ID: " + client.getId());
                System.out.print("Nom: " + client.getNom());
                System.out.print("Adresse: " + client.getAdresse());
                System.out.print("Téléphone: " + client.getTelephone());
            }
            System.out.println("les détails des composants de ce projet");

            projet.getComposants().forEach(composant -> {
                System.out.print("ID: " + composant.getId());
                System.out.print("Nom: " + composant.getNom());
                System.out.print("Type :" +composant.getTypeComposant());
                System.out.print("tva :" +composant.getTauxTVA());
                composant.getMateriaux().forEach(materiau -> {
                    System.out.print("ID: " + materiau.getId());
                    System.out.print("Nom: " + materiau.getNom());
                    System.out.print("Quantité: " + materiau.getQuantite());
                    System.out.print("cout transport : " + materiau.getCoutTransport());
                    System.out.print("coefficient de qualité : " + materiau.getCoefficientQualite());

                });
                composant.getMainOeuvres().forEach(mainOeuvre -> {
                    System.out.print("ID: " + mainOeuvre.getId());
                    System.out.print("Nom: " + mainOeuvre.getNom());
                    System.out.print("taux horaire : " + mainOeuvre.getTauxHoraire());
                    System.out.print("heures de travail  : " + mainOeuvre.getHeuresTravail());
                    System.out.print("productivité ouvrier : " + mainOeuvre.getProductiviteOuvrier());
                });
                
            });


                }
        );
    }
}
