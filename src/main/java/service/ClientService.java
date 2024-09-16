package service;

import bean.Client;
import dao.impl.ClientDaoImpl;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientService {
    private ClientDaoImpl clientDaoImpl;
    private Scanner scanner;

    public ClientService() {
        clientDaoImpl = new ClientDaoImpl();
        scanner = new Scanner(System.in);
    }

    public void save() {
        System.out.println("Entrer le nom du client: ");
        String nom = scanner.nextLine();
        System.out.println("Entrer l'adresse du Client: ");
        String adresse = scanner.nextLine();
        System.out.println("Entrer le telephone du Client: ");
        String telephone = scanner.nextLine();
        System.out.println("Le client est professionnel(true/false)?");
        boolean estProfessionnel = scanner.nextBoolean();
        Client client = new Client(nom, adresse, telephone, estProfessionnel);
        clientDaoImpl.save(client);
    }

    public void update() {
        System.out.println("Enter Client id: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Entrer le nom du client: ");
        String nom = scanner.nextLine();
        System.out.println("Entrer l'adresse du Client: ");
        String adresse = scanner.nextLine();
        System.out.println("Entrer le telephone du Client: ");
        String telephone = scanner.nextLine();
        System.out.println("Le client est professionnel(true/false)?");
        boolean estProfessionnel = scanner.nextBoolean();
        Client client = new Client(nom, adresse, telephone, estProfessionnel);
        clientDaoImpl.update(client);
    }

    public void delete() {
        System.out.println("Entrer Client id: ");
        int id = scanner.nextInt();
        clientDaoImpl.delete(id);
    }

    public Client findById() {
        System.out.println("Enter Client id: ");
        int id = scanner.nextInt();
        Optional<Client> optionalClient = clientDaoImpl.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        } else {
            System.out.println("Client non trouvé.");
            return null;
        }
    }

    public List<Client> findAll() {
        return clientDaoImpl.findAll();
    }

    public List<Client> findByNom(String nom) {
        System.out.println("Entrer le nom du client: ");
        String nomClient = scanner.nextLine();
        return clientDaoImpl.findByName(nomClient);
    }

}
