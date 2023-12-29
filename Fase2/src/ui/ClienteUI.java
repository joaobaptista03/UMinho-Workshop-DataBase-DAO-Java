package src.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import src.data.OficinaDAO;

public class ClienteUI {

    private int clienteID;
    private OficinaDAO oficinaDAO;
    private BufferedReader reader;

    public ClienteUI(OficinaDAO oficinaDAO, int clienteID) {
        this.clienteID = clienteID;
        this.oficinaDAO = oficinaDAO;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void execute() throws IOException {
        try {
            int choice;

            do {
                System.out.println("Menu do Cliente:");
                System.out.println("1. Consultar Estado do Serviço");
                System.out.println("2. Sair");
                System.out.print("Escolha uma opção: ");
                choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        System.out.print("Insira o ID do serviço: ");
                        int servicoID = Integer.parseInt(reader.readLine());
                        System.out.println("Estado do pedido: " + oficinaDAO.consultarEstadoServico(servicoID));
                        break;
                    case 2:
                        System.out.println("A sair do Menu Cliente...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (choice != 2);

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
