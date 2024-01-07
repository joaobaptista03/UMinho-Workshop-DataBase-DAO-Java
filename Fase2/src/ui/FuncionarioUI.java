package src.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import src.business.*;
import src.business.Servico.ServicoEstado;
import src.data.OficinaDAOFacade;

public class FuncionarioUI {

    private int funcionarioID;
    private OficinaDAOFacade oficinaDAO;
    private BufferedReader reader;

    public FuncionarioUI(OficinaDAOFacade oficinaDAO, int funcionarioID) {
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

                System.out.println();

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
            List<FuncionarioTurno> turnos = oficinaDAO.getTurnosFuncionario(funcionarioID);
            FuncionarioTurno ultimoTurno = turnos.isEmpty() ? null : turnos.get(turnos.size() - 1);

            if (ultimoTurno != null && ultimoTurno.getFim() == null) {
                ultimoTurno.setFim(LocalDateTime.now());
                oficinaDAO.updateTurno(ultimoTurno);
                System.out.println("Hora de saída registrada.");
            } else {
                FuncionarioTurno novoTurno = new FuncionarioTurno(oficinaDAO.getNrTurnos(), funcionarioID, LocalDateTime.now(), null);
                oficinaDAO.insertTurno(novoTurno);
                System.out.println("Hora de entrada registrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consultarHorario() {
        try {
            Funcionario funcionario = oficinaDAO.getFuncionario(funcionarioID);
    
            System.out.println("O seu horário establecido: " + funcionario.getNome() + ":");
            System.out.println("Hora de entrada: " + funcionario.getHoraEntrada());
            System.out.println("Hora de saída: " + funcionario.getHoraSaida());

            System.out.println("Os seus Serviços no próximo mês:");
            List<Servico> servicos = oficinaDAO.servicosEntreDatas(LocalDate.now(), LocalDate.now().plusMonths(1));
            for (Servico servico : servicos)
                if (servico.getFuncionario().getId() == funcionarioID)
                    System.out.println("Serviço " + servico.getId() + " - " + servico.getEstado() + " - " + servico.getDataHora() + " - " + servico.getVeiculo().getMatricula() + " - " + servico.getServicoTipo());

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void alterarEstadoServico() {
        try {
            System.out.print("Insira o ID do serviço: ");
            int servicoID = Integer.parseInt(reader.readLine());

            System.out.print("Insira o novo estado do serviço (Agendado / EmRealizacao / Realizado / Pago): ");
            String estadoString = reader.readLine();
            Servico.ServicoEstado estado = Servico.ServicoEstado.valueOf(estadoString);
    
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

        System.out.println("Terá de indicar duas datas para marcar o serviço entre elas.");
        System.out.println("Insira a primeira data e hora do serviço (dd/mm/aaaa): ");
        LocalDate data1 = LocalDate.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("Insira a segunda data e hora do serviço (dd/mm/aaaa): ");
        LocalDate data2 = LocalDate.parse(reader.readLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<Servico> servicos = oficinaDAO.servicosEntreDatas(data1, data2);
        for (Servico servico : servicos) {
            System.out.println("Serviço " + servico.getId() + " - " + servico.getEstado() + " - " + servico.getDataHora() + " - Funcionário " + servico.getFuncionario().getId());
        }
        if (servicos.isEmpty()) {
            System.out.println("Não existe nenhum serviço marcado para estas datas.\n");
        }

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

        Servico servico = new Servico(nrServicos, ServicoEstado.Agendado, dataHora, oficinaDAO.getFuncionario(funcionarioID), temp, oficinaDAO.getVeiculo(matricula), Servico.ServicoTipo.valueOf(tipoServico));
        oficinaDAO.insertServico(servico);
    }

    private void registarPagamentoServico() throws NumberFormatException, IOException {
        System.out.println("Insira o ID do serviço: ");
        int servicoID = Integer.parseInt(reader.readLine());

        Servico s = oficinaDAO.getServico(servicoID);
        s.setEstado(ServicoEstado.Pago);

        Fatura f = s.getFatura();
        f.setPago(true);

        oficinaDAO.updateFatura(f);
        oficinaDAO.updateServico(s);
    }
}
