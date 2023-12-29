package src.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import src.data.OficinaDAO;

public class FuncionarioUI {

    private OficinaDAO oficinaDAO;
    private BufferedReader reader;

    public FuncionarioUI(OficinaDAO oficinaDAO) {
        this.oficinaDAO = oficinaDAO;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void execute() {
        try {
            int choice;

            do {
                System.out.println("Escolha uma opção para funcionários:");
                System.out.println("1. Consultar Informações de um Carro");
                System.out.println("2. Dar Entrada e Saída do Turno");
                System.out.println("3. Consultar Horário");
                System.out.println("4. Alterar Estado do Serviço");
                System.out.println("5. Agendar Serviço");
                System.out.println("6. Registar Pagamento do Serviço");
                System.out.println("7. Sair.");

                choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        consultarInformacoesCarro();
                        break;
                    case 2:
                        darEntradaSaidaTurno();
                        break;
                    case 3:
                        consultarHorario();
                        break;
                    case 4:
                        alterarEstadoServico();
                        break;
                    case 5:
                        agendarServico();
                        break;
                    case 6:
                        registarPagamentoServico();
                        break;
                    case 7:
                        System.out.println("A sair do Menu Funcionário...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (choice != 7);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void consultarInformacoesCarro() {
        // consultar informações de um carro
    }

    private void darEntradaSaidaTurno() {
        // dar entrada e saída do turno
    }

    private void consultarHorario() {
        // consultar o horário
    }

    private void alterarEstadoServico() {
        // alterar o estado do serviço
    }

    private void agendarServico() {
        // agendar serviço
    }

    private void registarPagamentoServico() {
        // registar pagamento de serviço
    }
}
