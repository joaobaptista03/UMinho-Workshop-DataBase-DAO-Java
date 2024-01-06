package src.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import src.business.*;
import src.data.OficinaDAO;

public class FuncionarioUI {

    private int funcionarioID;
    private OficinaDAO oficinaDAO;
    private BufferedReader reader;

    public FuncionarioUI(OficinaDAO oficinaDAO, int funcionarioID) {
        this.funcionarioID = funcionarioID;
        this.oficinaDAO = oficinaDAO;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void execute() throws ParseException {
        try {
            int choice;

            do {
                System.out.println("\nMenu do Funcionário:");
                System.out.println("1. Consultar Informações de um Carro");
                System.out.println("2. Dar Entrada e Saída do Turno");
                System.out.println("3. Consultar Horário");
                System.out.println("4. Alterar Estado do Serviço");
                System.out.println("5. Agendar Serviço");
                System.out.println("6. Registar Pagamento do Serviço");
                System.out.println("7. Sair.");
                System.out.print("Escolha uma opção: ");

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
        try {
            System.out.print("Insira a matrícula do carro: \n");
            String matricula = reader.readLine();
            
            Veiculo carro = oficinaDAO.getVeiculo(matricula);
    
            if (carro != null) {
                System.out.println("Informações do Carro:");
                System.out.println("Matrícula: " + carro.getMatricula());
                System.out.println("Marca: " + carro.getMarca());
                System.out.println("Modelo: " + carro.getModelo());
                System.out.println("Tipo de Motor: " + carro.getTipoMotor());
                System.out.println("Ano: " + carro.getInformacoes());
            } else {
                System.out.println("Carro não encontrado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void darEntradaSaidaTurno() {
        try {
            List<Turno> turnos = oficinaDAO.getTurnosFuncionario(funcionarioID);
            Turno ultimoTurno = turnos.isEmpty() ? null : turnos.get(turnos.size() - 1);

            if (ultimoTurno != null && ultimoTurno.getFim() == null) {
                ultimoTurno.setFim(LocalDateTime.now());
                oficinaDAO.updateTurno(ultimoTurno);
                System.out.println("Hora de saída registrada.");
            } else {
                Turno novoTurno = new Turno(0, funcionarioID, LocalDateTime.now(), null);
                oficinaDAO.insertTurno(novoTurno);
                System.out.println("Hora de entrada registrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consultarHorario() {
        try {
            System.out.println("Insira o ID do funcionário: ");
    
            Funcionario funcionario = oficinaDAO.getFuncionario(funcionarioID);
    
            System.out.println("Horário de trabalho do funcionário " + funcionario.getNome() + ":");
            System.out.println("Hora de entrada: " + funcionario.getHoraEntrada());
            System.out.println("Hora de saída: " + funcionario.getHoraSaida());

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void alterarEstadoServico() {
        try {
            System.out.print("Insira o ID do serviço: ");
            int servicoID = Integer.parseInt(reader.readLine());

            System.out.print("Insira o novo estado do serviço: ");
            String estado = reader.readLine();
    
            Servico novoServico = oficinaDAO.getServico(servicoID);
            if (novoServico == null) {
                System.out.println("Serviço não encontrado.");
                return;
            }
            novoServico.setEstado(estado);
            oficinaDAO.updateServico(novoServico);

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void agendarServico() throws IOException, ParseException {
        int nrServicos = oficinaDAO.getNrServicos();

        System.out.println("Insira o ID do cliente: ");
        int clienteID = Integer.parseInt(reader.readLine());

        System.out.println("Insira a data e hora do serviço (dd/mm/aaaa hh:mm): ");
        LocalDateTime dataHora = LocalDateTime.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        System.out.println("Insira a matricula do carro: ");
        String matricula = reader.readLine();

        System.out.println("Insira o tipo de serviço: ");
        String tipoServico = reader.readLine();

        System.out.println("Insira o preço: ");
        float preco = Float.parseFloat(reader.readLine());

        Fatura temp = new Fatura(oficinaDAO.getNrFaturas(), oficinaDAO.getCliente(clienteID), preco, false);
        oficinaDAO.insertFatura(temp);

        Servico servico = new Servico(nrServicos, "Agendado", dataHora, oficinaDAO.getFuncionario(funcionarioID), temp, oficinaDAO.getVeiculo(matricula), ServicoTipo.valueOf(tipoServico));
        oficinaDAO.insertServico(servico);
    }

    private void registarPagamentoServico() throws NumberFormatException, IOException {
        System.out.println("Insira o ID do serviço: ");
        int servicoID = Integer.parseInt(reader.readLine());

        Fatura f = oficinaDAO.getServico(servicoID).getFatura();
        f.setPago(true);

        oficinaDAO.updateFatura(f);
    }
}
