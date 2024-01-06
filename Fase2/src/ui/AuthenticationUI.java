package src.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import src.business.Cliente;
import src.data.OficinaDAOFacade;

public class AuthenticationUI {
    public static class AuthTypeID {
        public int authType;
        public int id;
    
        public AuthTypeID(int authType, int id) {
            this.authType = authType;
            this.id = id;
        }
    }

    public static AuthTypeID authenticate(OficinaDAOFacade oficinaDAO) throws NumberFormatException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose your role: (1) Cliente, (2) Funcionário, (3) Administrator");
        int userTypeChoice = Integer.parseInt(reader.readLine());

        switch (userTypeChoice) {
            case 1:
                System.out.println("Já tens conta? (s/n)");
                String registeredClientChoice = reader.readLine().toLowerCase();

                if (registeredClientChoice.equals("s")) {
                    System.out.println("Digita o teu e-mail:");
                    String clientUsername = reader.readLine();
                    System.out.println("Digita a tua palavra-passe:");
                    String clientPassword = reader.readLine();

                    int clienteID = oficinaDAO.authenticateCliente(clientUsername, clientPassword);

                    if (clienteID != -1) return new AuthTypeID(1, clienteID);
                    return null;

                } else if (registeredClientChoice.equals("n")) {
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

                    if (!oficinaDAO.insertCliente(newCliente)) return null;
                    return new AuthTypeID(1, id);
                } else {
                    System.out.println("Opção inválida. A sair...");
                    return null;
                }

            case 2:
                boolean funcionarioAuthenticated = false;
                String funcionarioUsername = "";
                String funcionarioPassword = "";
                int funcionarioID = -1;
                do {
                    System.out.println("Digita o teu email:");
                    funcionarioUsername = reader.readLine();
                    System.out.println("Digita a tua password:");
                    funcionarioPassword = reader.readLine();

                    funcionarioID = oficinaDAO.authenticateFuncionario(funcionarioUsername, funcionarioPassword);
                    if (funcionarioID != -1) funcionarioAuthenticated = true;
                } while (!funcionarioAuthenticated);

                return new AuthTypeID(2, funcionarioID);

            case 3:
                boolean adminAuthenticated = false;
                String adminUsername = "";
                String adminPassword = "";
                int adminID = -1;
                do {
                    System.out.println("Digita o teu email:");
                    adminUsername = reader.readLine();
                    System.out.println("Digita a tua password:");
                    adminPassword = reader.readLine();

                    adminID = oficinaDAO.authenticateAdministrator(adminUsername, adminPassword);
                    if (adminID != -1) adminAuthenticated = true;
                } while (!adminAuthenticated);

                return new AuthTypeID(3, adminID);

            default:
                System.out.println("Opção inválida. A sair...");
                return null;
        }
    }
}
