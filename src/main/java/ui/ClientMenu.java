package ui;

import bean.Client;
import service.ClientService;

import java.util.List;
import java.util.Scanner;

public class ClientMenu {
    private ClientService clientService;
    private Scanner scanner;
    public ClientMenu(ClientService clientService) {
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }
    public void clientMenu()  {
        while (true) {
            System.out.println("1. Create Client");
            System.out.println("2. Update Client");
            System.out.println("3. Delete Client by id");
            System.out.println("4. Show Client by id");
            System.out.println("5. show all clients ");
            System.out.print("Enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                   clientService.save();
                   break;
                case 2:
                    clientService.update();
                    break;
                case 3:
                    clientService.delete();
                    break;
                case 4:
                    Client client = clientService.findById();
                    System.out.println("client :"+client.getNom()+ ", adresse :"+client.getAdresse()+", tel :"+client.getTelephone());
                    break;
                case 5:
                    List<Client> clients = clientService.findAll();
                     break;

                case 6:
                    List<Client> clients1=clientService.findByNom();
                    break;
                case 7:
                    System.out.println("Bye!");
                    break;
            }
        }
    }


}

