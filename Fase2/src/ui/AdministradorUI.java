package src.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import src.business.*;
import src.data.OficinaDAO;

public class AdministradorUI {
    
    private int administradorID;
    private OficinaDAO oficinaDAO;
    private BufferedReader reader;

    public AdministradorUI(OficinaDAO oficinaDAO, int administradorID) {
        this.administradorID = administradorID;
        this.oficinaDAO = oficinaDAO;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void execute() throws IOException {
        try {
            int choice;

            do {
                System.out.println("Menu do Administrador:");
                System.out.println("1. Adicionar Funcionário");
                System.out.println("2. Sair");
                System.out.print("Escolha uma opção: ");
                choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        adicionarFuncionario(oficinaDAO);
                        break;
                    case 2:
                        System.out.println("A sair do Menu Administrador...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (choice != 2);
       
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private boolean adicionarFuncionario(OficinaDAO oficinaDAO) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\nAdicionar Funcionário:");
        
        System.out.print("Nome do Funcionário: ");
        String nome = reader.readLine();
        
        System.out.print("Email do Funcionário: ");
        String email = reader.readLine();
        
        System.out.print("Password do Funcionário: ");
        String password = reader.readLine();
        
        System.out.print("Número do Cartão do Funcionário: ");
        int nrCartao = Integer.parseInt(reader.readLine());
        
        System.out.print("Posto do Funcionário: ");
        String posto = reader.readLine();

        System.out.print("Hora de Entrada do Funcionário (HH:MM): ");
        String horaEntradaStr = reader.readLine();
        Time horaEntrada = Time.valueOf(LocalTime.parse(horaEntradaStr));

        System.out.print("Hora de Saída do Funcionário (HH:MM): ");
        String horaSaidaStr = reader.readLine();
        Time horaSaida = Time.valueOf(LocalTime.parse(horaSaidaStr));

        System.out.print("Competências do Funcionário (separadas por vírgula): ");
        String competencias = reader.readLine();
        List<String> competencias_list = Arrays.asList(competencias.split(","));

        int nrFuncionarios = oficinaDAO.getNrFuncionarios();
        Funcionario novoFuncionario = new Funcionario(nrFuncionarios, nome, email, password, nrCartao, posto, horaEntrada, horaSaida, competencias_list, administradorID);
        
        if (!oficinaDAO.insertFuncionario(novoFuncionario)) {
            System.err.println("Erro ao adicionar Funcionário!");
            return false;
        }

        System.out.println("Funcionário adicionado com sucesso!");
        return true;
    }
}