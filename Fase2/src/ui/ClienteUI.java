package src.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import src.data.OficinaDAOFacade;

public class ClienteUI {

    private OficinaDAOFacade oficinaDAO;
    private BufferedReader reader;

    public ClienteUI(OficinaDAOFacade oficinaDAO) {
        this.oficinaDAO = oficinaDAO;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void execute() throws IOException {
        try {
            int choice;

            do {
                System.out.println("\nMenu do Cliente:");
                System.out.println("1. Consultar Estado do Serviço");
                System.out.println("2. Sair");
                System.out.print("Escolha uma opção: ");
                choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        System.out.print("Insira o ID do serviço: ");
                        int servicoID = Integer.parseInt(reader.readLine());
                        String estado = oficinaDAO.consultarEstadoServico(servicoID);

                        if (estado != null) System.out.println("Estado do pedido: " + estado);
                        else System.out.println("Serviço não encontrado.");
                        
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
