package src.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import src.business.Cliente;
import src.data.OficinaDAO;

public class Authentication {
    public static int authenticate(OficinaDAO oficinaDAO) throws NumberFormatException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose your role: (1) Cliente, (2) Funcionário, (3) Administrator");
        int userTypeChoice = Integer.parseInt(reader.readLine());

        switch (userTypeChoice) {
            case 1:
                System.out.println("Are you a registered client? (yes/no)");
                String registeredClientChoice = reader.readLine().toLowerCase();

                if (registeredClientChoice.equals("yes")) {
                    System.out.println("Digita o teu e-mail:");
                    String clientUsername = reader.readLine();
                    System.out.println("Digita a tua palavra-passe:");
                    String clientPassword = reader.readLine();

                    if (oficinaDAO.authenticateCliente(clientUsername, clientPassword)) return 1;
                    return -1;

                } else if (registeredClientChoice.equals("no")) {
                    System.out.println("Criar uma nova conta:");
                    System.out.println("Digita o e-mail para a nova conta:");
                    String newClientEmail = reader.readLine();
                    System.out.println("Digita a palavra-passe para a nova conta: ");
                    String newClientPassword = reader.readLine();
                    System.out.println("Digita o teu nome: ");
                    String newClientName = reader.readLine();
                    System.out.println("Digita o teu NIF: ");
                    int newClientNIF = Integer.parseInt(reader.readLine());
                    System.out.println("Digita a tua morada: ");
                    String newClientAddress = reader.readLine();
                    System.out.println("Digita o teu telefone: ");
                    int newClientTelephone = Integer.parseInt(reader.readLine());

                    int id = oficinaDAO.getNrClientes();
                    Cliente newCliente = new Cliente(id, newClientName, newClientNIF, newClientAddress, newClientEmail, newClientPassword, newClientTelephone);

                    oficinaDAO.insertCliente(newCliente);
                    return 2;
                } else {
                    System.out.println("Invalid choice. Exiting...");
                    return -1;
                }

            case 2:
                boolean funcionarioAuthenticated = false;
                String funcionarioUsername = "";
                String funcionarioPassword = "";
                do {
                    System.out.println("Enter your username:");
                    funcionarioUsername = reader.readLine();
                    System.out.println("Enter your password:");
                    funcionarioPassword = reader.readLine();

                    funcionarioAuthenticated = authenticateFuncionario(funcionarioUsername, funcionarioPassword);
                } while (!funcionarioAuthenticated);

                return 3;

            case 3:
                boolean adminAuthenticated = false;
                String adminUsername = "";
                String adminPassword = "";
                do {
                    System.out.println("Enter your username:");
                    adminUsername = reader.readLine();
                    System.out.println("Enter your password:");
                    adminPassword = reader.readLine();

                    adminAuthenticated = authenticateAdministrator(adminUsername, adminPassword);
                } while (!adminAuthenticated);

                return 4;

            default:
                System.out.println("Invalid choice. Exiting...");
                return -1;
        }
    }

    private static boolean authenticateFuncionario(String username, String password) {
        if ("funcionarioUser".equals(username) && "funcionarioPassword".equals(password)) {
            System.out.println("Authentication successful for Funcionario!");
            return true;
        } else {
            System.out.println("Authentication failed for Funcionario. Try again...");
            return false;
        }
    }

    private static boolean authenticateAdministrator(String username, String password) {
        if ("adminUser".equals(username) && "adminPassword".equals(password)) {
            System.out.println("Authentication successful for Administrator!");
            return true;
        } else {
            System.out.println("Authentication failed for Administrator. Try again...");
            return false;
        }
    }
}
