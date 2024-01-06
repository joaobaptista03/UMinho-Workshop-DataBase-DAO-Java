package src.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import src.business.*;
import src.business.Veiculo.TipoMotor;

public class OficinaDAO implements OficinaDAOInterface{

    public OficinaDAO() throws IOException {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD)) {
            executeScript("src/data/CreateDB.sql", connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeScript(String scriptFileName, Connection connection) throws IOException, SQLException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(scriptFileName)) {
            if (inputStream == null) {
                throw new IOException("Script file not found: " + scriptFileName);
            }
    
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder scriptContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    scriptContent.append(line).append("\n");
                }
    
                String[] queries = scriptContent.toString().split(";");
    
                try (Statement statement = connection.createStatement()) {
                    for (String query : queries) {
                        if (!query.trim().isEmpty()) {
                            statement.addBatch(query);
                        }
                    }
                    statement.executeBatch();
                }
            }
        }
    }    

    public boolean insertCliente(Cliente cliente) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement checkIfExists = connection.prepareStatement("SELECT COUNT(*) FROM clientes WHERE email = ? OR nif = ? OR telefone = ?");
             PreparedStatement insertCliente = connection.prepareStatement("INSERT INTO clientes VALUES (?, ?, ?, ?, ?, ?, ?)")) {
    
            checkIfExists.setString(1, cliente.getEmail());
            checkIfExists.setInt(2, cliente.getNif());
            checkIfExists.setInt(3, cliente.getTelefone());

    
            try (ResultSet resultSet = checkIfExists.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return false;
                }
            }
    
            insertCliente.setInt(1, cliente.getId());
            insertCliente.setString(2, cliente.getNome());
            insertCliente.setInt(3, cliente.getNif());
            insertCliente.setString(4, cliente.getMorada());
            insertCliente.setString(5, cliente.getEmail());
            insertCliente.setString(6, cliente.getPassword());
            insertCliente.setInt(7, cliente.getTelefone());
    
            insertCliente.executeUpdate();
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void updateCliente(Cliente cliente) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE clientes SET nome = ?, nif = ?, morada = ?, email = ?, password = ?, telefone = ? WHERE id = ?")) {

            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setInt(2, cliente.getNif());
            preparedStatement.setString(3, cliente.getMorada());
            preparedStatement.setString(4, cliente.getEmail());
            preparedStatement.setString(5, cliente.getPassword());
            preparedStatement.setInt(6, cliente.getTelefone());
            preparedStatement.setInt(7, cliente.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCliente(int clienteId) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD)) {
    
            try (PreparedStatement deleteFaturas = connection.prepareStatement("DELETE FROM faturas WHERE clienteId = ?")) {
                deleteFaturas.setInt(1, clienteId);
                deleteFaturas.executeUpdate();
            }
    
            try (PreparedStatement deleteCliente = connection.prepareStatement("DELETE FROM clientes WHERE id = ?")) {
                deleteCliente.setInt(1, clienteId);
                deleteCliente.executeUpdate();
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrClientes() {
        int nrClientes = 0;
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM clientes")) {

            if (resultSet.next()) {
                nrClientes = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nrClientes;
    }

    public int authenticateCliente(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM clientes WHERE email = ? AND password = ?")) {
    
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Cliente getCliente(int id) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clientes WHERE id = ?")) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    int nif = resultSet.getInt("nif");
                    String morada = resultSet.getString("morada");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    int telefone = resultSet.getInt("telefone");

                    return new Cliente(id, nome, nif, morada, email, password, telefone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertFuncionario(Funcionario funcionario) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement checkIfExists = connection.prepareStatement("SELECT COUNT(*) FROM funcionarios WHERE email = ? OR password = ? OR nrCartao = ?");
             PreparedStatement insertFuncionario = connection.prepareStatement("INSERT INTO funcionarios VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
    
            checkIfExists.setString(1, funcionario.getEmail());
            checkIfExists.setString(2, funcionario.getPassword());
            checkIfExists.setInt(3, funcionario.getNrCartao());
    
            try (ResultSet resultSet = checkIfExists.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return false;
                }
            }
    
            insertFuncionario.setInt(1, funcionario.getId());
            insertFuncionario.setString(2, funcionario.getNome());
            insertFuncionario.setString(3, funcionario.getEmail());
            insertFuncionario.setString(4, funcionario.getPassword());
            insertFuncionario.setInt(5, funcionario.getNrCartao());
            insertFuncionario.setString(6, funcionario.getPosto());
            insertFuncionario.setTime(7, Time.valueOf(funcionario.getHoraEntrada()));
            insertFuncionario.setTime(8, Time.valueOf(funcionario.getHoraSaida()));
    
            List<String> competencias = funcionario.getCompetencias();
            String competenciasString = competencias.stream().collect(Collectors.joining(","));
            insertFuncionario.setString(9, competenciasString);
    
            insertFuncionario.setInt(10, funcionario.getAdministradorAdicionado());
    
            insertFuncionario.executeUpdate();
    
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateFuncionario(Funcionario funcionario) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE funcionarios SET nome = ?, nrCartao = ?, email = ?, password = ?, posto = ?, horaEntrada = ?, horaSaida = ?, competencias = ?, administradorAdicionado = ? WHERE id = ?")) {

            preparedStatement.setString(1, funcionario.getNome());
            preparedStatement.setInt(2, funcionario.getNrCartao());
            preparedStatement.setString(3, funcionario.getEmail());
            preparedStatement.setString(4, funcionario.getPassword());
            preparedStatement.setString(5, funcionario.getPosto());
            preparedStatement.setTime(6, Time.valueOf(funcionario.getHoraEntrada()));
            preparedStatement.setTime(7, Time.valueOf(funcionario.getHoraSaida()));

            List<String> competencias = funcionario.getCompetencias();
            String competenciasString = competencias.stream().collect(Collectors.joining(","));
            preparedStatement.setString(8, competenciasString);

            preparedStatement.setInt(9, funcionario.getId());
            preparedStatement.setInt(10, funcionario.getAdministradorAdicionado());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFuncionario(int funcionarioId) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD)) {
    
            try (PreparedStatement deleteServicos = connection.prepareStatement("DELETE FROM servicos WHERE funcionarioId = ?")) {
                deleteServicos.setInt(1, funcionarioId);
                deleteServicos.executeUpdate();
            }
    
            try (PreparedStatement deleteFuncionario = connection.prepareStatement("DELETE FROM funcionarios WHERE id = ?")) {
                deleteFuncionario.setInt(1, funcionarioId);
                deleteFuncionario.executeUpdate();
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrFuncionarios() {
        int nrFuncionarios = 0;
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM funcionarios")) {

            if (resultSet.next()) {
                nrFuncionarios = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nrFuncionarios;
    }

    public int authenticateFuncionario(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM funcionarios WHERE email = ? AND password = ?")) {
    
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Funcionario getFuncionario(int id) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM funcionarios WHERE id = ?")) {

            preparedStatement.setInt(1, id);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    int nrCartao = resultSet.getInt("nrCartao");
                    String posto = resultSet.getString("posto");
                    LocalTime horaEntrada = resultSet.getTime("horaEntrada").toLocalTime();
                    LocalTime horaSaida = resultSet.getTime("horaSaida").toLocalTime();
                    List<FuncionarioTurno> turnos = this.getTurnosFuncionario(id);
                    String competenciasString = resultSet.getString("competencias");

                    List<String> competencias = Arrays.asList(competenciasString.split(","));

                    int administradorAdicionado = resultSet.getInt("administradorAdicionado");

                    return new Funcionario(id, nome, email, password, nrCartao, posto, horaEntrada, horaSaida, turnos, competencias, administradorAdicionado);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FuncionarioTurno> getTurnosFuncionario(int funcionarioId) {
        List<FuncionarioTurno> turnos = new ArrayList<>();
        String query = "SELECT * FROM turnos WHERE funcionario_id = ?";

        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, funcionarioId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int turnoId = resultSet.getInt("id");
                    LocalDateTime inicio = resultSet.getTimestamp("inicio").toLocalDateTime();
                    
                    Timestamp fimTimestamp = resultSet.getTimestamp("fim");
                    LocalDateTime fim = null;
                    if (fimTimestamp != null) {
                        fim = fimTimestamp.toLocalDateTime();
                    }

                    FuncionarioTurno turno = new FuncionarioTurno(turnoId, funcionarioId, inicio, fim);
                    turnos.add(turno);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return turnos;
    }

    public void insertTurno(FuncionarioTurno turno) {
        String query = "INSERT INTO turnos VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, turno.getId());
            preparedStatement.setInt(2, turno.getIdFuncionario());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(turno.getInicio()));
            if (turno.getFim() != null) {
                preparedStatement.setTimestamp(4, Timestamp.valueOf(turno.getFim()));
            } else {
                preparedStatement.setTimestamp(4, null);
            }
    
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTurno(FuncionarioTurno turno) {
        String query = "UPDATE turnos SET inicio = ?, fim = ?";
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setTimestamp(1, Timestamp.valueOf(turno.getInicio()));
            if (turno.getFim() != null) {
                preparedStatement.setTimestamp(2, Timestamp.valueOf(turno.getFim()));
            } else {
                preparedStatement.setTimestamp(2, null);
            }
    
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTurno(int turnoID) {
        String query = "DELETE FROM turnos WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, turnoID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrTurnos() {
        String query = "SELECT COUNT(*) FROM turnos";
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public FuncionarioTurno getTurno(int turnoId) {
        String query = "SELECT * FROM turnos WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, turnoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int funcionarioId = resultSet.getInt("funcionario_id");
                    LocalDateTime inicio = resultSet.getTimestamp("inicio").toLocalDateTime();
                    LocalDateTime fim = resultSet.getTimestamp("fim").toLocalDateTime();
    
                    return new FuncionarioTurno(id, funcionarioId, inicio, fim);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public void insertVeiculo(Veiculo veiculo) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO veiculos VALUES (?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, veiculo.getMatricula());
            preparedStatement.setString(2, veiculo.getMarca());
            preparedStatement.setString(3, veiculo.getModelo());
            preparedStatement.setString(4, veiculo.getTipoMotor().name());
            preparedStatement.setString(5, veiculo.getInformacoes());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVeiculo(Veiculo veiculo) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE veiculos SET marca = ?, modelo = ?, tipo_motor = ?, informacoes = ? WHERE matricula = ?")) {

            preparedStatement.setString(1, veiculo.getMarca());
            preparedStatement.setString(2, veiculo.getModelo());
            preparedStatement.setString(3, veiculo.getTipoMotor().name());
            preparedStatement.setString(4, veiculo.getInformacoes());
            preparedStatement.setString(5, veiculo.getMatricula());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteVeiculo(String matricula) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD)) {

            try (PreparedStatement deleteServicos = connection.prepareStatement("DELETE FROM servicos WHERE veiculoMatricula = ?")) {
                deleteServicos.setString(1, matricula);
                deleteServicos.executeUpdate();
            }

            try (PreparedStatement deleteVeiculo = connection.prepareStatement("DELETE FROM veiculos WHERE matricula = ?")) {
                deleteVeiculo.setString(1, matricula);
                deleteVeiculo.executeUpdate();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrVeiculos() {
        int nrVeiculos = 0;
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM veiculos")) {

            if (resultSet.next()) {
                nrVeiculos = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nrVeiculos;
    }

    public Veiculo getVeiculo(String matricula) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM veiculos WHERE matricula = ?")) {
    
            preparedStatement.setString(1, matricula);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String marca = resultSet.getString("marca");
                    String modelo = resultSet.getString("modelo");
                    String tipoMotorString = resultSet.getString("tipo_motor");
                    TipoMotor tipoMotor = TipoMotor.valueOf(tipoMotorString);
                    String informacoes = resultSet.getString("informacoes");
                
                    return new Veiculo(matricula, marca, modelo, tipoMotor, informacoes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertFatura(Fatura fatura) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO faturas VALUES (?, ?, ?, ?)")) {

            preparedStatement.setInt(1, fatura.getNrFatura());
            preparedStatement.setInt(2, fatura.getCliente().getId());
            preparedStatement.setFloat(3, fatura.getPreco());
            preparedStatement.setBoolean(4, fatura.getPago());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFatura(Fatura fatura) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE faturas SET clienteId = ?, preco = ?, pago = ? WHERE nrFatura = ?")) {

            preparedStatement.setInt(1, fatura.getCliente().getId());
            preparedStatement.setFloat(2, fatura.getPreco());
            preparedStatement.setBoolean(3, fatura.getPago());
            preparedStatement.setInt(4, fatura.getNrFatura());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFatura(int nrFatura) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD)) {

            try (PreparedStatement deleteServicos = connection.prepareStatement("DELETE FROM servicos WHERE faturaNr = ?")) {
                deleteServicos.setInt(1, nrFatura);
                deleteServicos.executeUpdate();
            }
    
            try (PreparedStatement deleteFatura = connection.prepareStatement("DELETE FROM faturas WHERE nrFatura = ?")) {
                deleteFatura.setInt(1, nrFatura);
                deleteFatura.executeUpdate();
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrFaturas() {
        int nrFaturas = 0;
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM faturas")) {

            if (resultSet.next()) {
                nrFaturas = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nrFaturas;
    }

    public Fatura getFatura(int nrFatura) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM faturas WHERE nrFatura = ?")) {
    
            preparedStatement.setInt(1, nrFatura);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int clienteId = resultSet.getInt("clienteId");
                    float preco = resultSet.getFloat("preco");
                    boolean pago = resultSet.getBoolean("pago");

                    Cliente cliente = getCliente(clienteId);
                
                    return new Fatura(nrFatura, cliente, preco, pago);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void insertServico(Servico servico) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO servicos VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setInt(1, servico.getId());
            preparedStatement.setString(2, servico.getEstado());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(servico.getDataHora()));
            preparedStatement.setInt(4, servico.getFuncionario().getId());
            preparedStatement.setInt(5, servico.getFatura().getNrFatura());
            preparedStatement.setString(6, servico.getVeiculo().getMatricula());
            preparedStatement.setString(7, servico.getServicoTipo().name());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateServico(Servico servico) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE servicos SET estado = ?, dataHora = ?, funcionarioId = ?, faturaNr = ?, veiculoMatricula = ?, servicoTipo = ? WHERE id = ?")) {

            preparedStatement.setString(1, servico.getEstado());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(servico.getDataHora()));
            preparedStatement.setInt(3, servico.getFuncionario().getId());
            preparedStatement.setInt(4, servico.getFatura().getNrFatura());
            preparedStatement.setString(5, servico.getVeiculo().getMatricula());
            preparedStatement.setString(6, servico.getServicoTipo().name());
            preparedStatement.setInt(7, servico.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteServico(int servicoId) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM servicos WHERE id = ?")) {

            preparedStatement.setInt(1, servicoId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrServicos() {
        int nrServicos = 0;
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM servicos")) {

            if (resultSet.next()) {
                nrServicos = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nrServicos;
    }

    public String consultarEstadoServico(int servicoId) {
        String estadoServico = null;
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT estado FROM servicos WHERE id = ?")) {
    
            preparedStatement.setInt(1, servicoId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    estadoServico = resultSet.getString("estado");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estadoServico;
    }    

    public Servico getServico(int id) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM servicos WHERE id = ?")) {
    
            preparedStatement.setInt(1, id);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String estado = resultSet.getString("estado");
                    LocalDateTime dataHora = resultSet.getTimestamp("dataHora").toLocalDateTime();
                    int funcionarioId = resultSet.getInt("funcionarioId");
                    int faturaNr = resultSet.getInt("faturaNr");
                    String veiculoMatricula = resultSet.getString("veiculoMatricula");
                    String servicoTipoString = resultSet.getString("servicoTipo");
                    ServicoTipo servicoTipo = ServicoTipo.valueOf(servicoTipoString);
                
                    Funcionario funcionario = getFuncionario(funcionarioId);
                    Fatura fatura = getFatura(faturaNr);
                    Veiculo veiculo = getVeiculo(veiculoMatricula);
                
                    return new Servico(id, estado, dataHora, funcionario, fatura, veiculo, servicoTipo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertAdministrador(Administrator administrator) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO administradores VALUES (?, ?, ?, ?)")) {

            preparedStatement.setInt(1, administrator.getId());
            preparedStatement.setString(2, administrator.getNome());
            preparedStatement.setString(3, administrator.getEmail());
            preparedStatement.setString(4, administrator.getPassword());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAdministrador(Administrator administrator) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE administradores SET nome = ?, email = ?, password = ? WHERE id = ?")) {

            preparedStatement.setString(1, administrator.getNome());
            preparedStatement.setString(2, administrator.getEmail());
            preparedStatement.setString(3, administrator.getPassword());
            preparedStatement.setInt(4, administrator.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAdministrador(int administradorID) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM administradores WHERE id = ?")) {

            preparedStatement.setInt(1, administradorID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrAdministradores() {
        int nrAdministradores = 0;
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM administradores")) {

            if (resultSet.next()) {
                nrAdministradores = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nrAdministradores;
    }

    public int authenticateAdministrator(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM administradores WHERE email = ? AND password = ?")) {
    
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Administrator getAdministrador(int id) {
        try (Connection connection = DriverManager.getConnection(DAOConfig.URL + "OficinaDB", DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM administradores WHERE id = ?")) {
    
            preparedStatement.setInt(1, id);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                
                    return new Administrator(id, nome, email, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}