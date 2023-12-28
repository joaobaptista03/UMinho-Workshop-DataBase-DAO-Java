package src.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import src.business.Cliente;
import src.business.Funcionario;
import src.business.Veiculo;
import src.business.Fatura;
import src.business.Servico;

public class OficinaDAO {

    private static final String URL = DAOConfig.URL;
    private static final String USERNAME = DAOConfig.USERNAME;
    private static final String PASSWORD = DAOConfig.PASSWORD;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public OficinaDAO() throws IOException {
        try (Connection connection = getConnection();) {
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

    public void insertCliente(Cliente cliente) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO clientes VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setInt(1, cliente.getId());
            preparedStatement.setString(2, cliente.getNome());
            preparedStatement.setInt(3, cliente.getNif());
            preparedStatement.setString(4, cliente.getMorada());
            preparedStatement.setString(5, cliente.getEmail());
            preparedStatement.setString(6, cliente.getPassword());
            preparedStatement.setInt(7, cliente.getTelefone());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCliente(Cliente cliente) {
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM clientes WHERE id = ?")) {

            preparedStatement.setInt(1, clienteId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertFuncionario(Funcionario funcionario) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO funcionarios VALUES (?, ?, ?, ?, ?)")) {

            preparedStatement.setInt(1, funcionario.getId());
            preparedStatement.setString(2, funcionario.getNome());
            preparedStatement.setInt(3, funcionario.getNrCartao());
            preparedStatement.setString(4, funcionario.getPosto());

            List<String> competencias = funcionario.getCompetencias();
            String competenciasString = competencias.stream().collect(Collectors.joining(","));
            preparedStatement.setString(5, competenciasString);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFuncionario(Funcionario funcionario) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE funcionarios SET nome = ?, nrCartao = ?, posto = ?, competencias = ? WHERE id = ?")) {

            preparedStatement.setString(1, funcionario.getNome());
            preparedStatement.setInt(2, funcionario.getNrCartao());
            preparedStatement.setString(3, funcionario.getPosto());

            List<String> competencias = funcionario.getCompetencias();
            String competenciasString = competencias.stream().collect(Collectors.joining(","));
            preparedStatement.setString(5, competenciasString);

            preparedStatement.setInt(5, funcionario.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFuncionario(int funcionarioId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM funcionarios WHERE id = ?")) {

            preparedStatement.setInt(1, funcionarioId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertVeiculo(Veiculo veiculo) {
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection();
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
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM veiculos WHERE matricula = ?")) {

            preparedStatement.setString(1, matricula);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertFatura(Fatura fatura) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO faturas VALUES (?, ?)")) {

            preparedStatement.setInt(1, fatura.getNrFatura());
            preparedStatement.setInt(2, fatura.getCliente().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFatura(Fatura fatura) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE faturas SET clienteId = ? WHERE nrFatura = ?")) {

            preparedStatement.setInt(1, fatura.getCliente().getId());
            preparedStatement.setInt(2, fatura.getNrFatura());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFatura(int nrFatura) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM faturas WHERE nrFatura = ?")) {

            preparedStatement.setInt(1, nrFatura);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void insertServico(Servico servico) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO servicos VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setInt(1, servico.getId());
            preparedStatement.setString(2, servico.getEstado());
            preparedStatement.setTimestamp(3, new java.sql.Timestamp(servico.getDataHora().getTime()));
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
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE servicos SET estado = ?, dataHora = ?, funcionarioId = ?, faturaNr = ?, veiculoMatricula = ?, servicoTipo = ? WHERE id = ?")) {

            preparedStatement.setString(1, servico.getEstado());
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(servico.getDataHora().getTime()));
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
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM servicos WHERE id = ?")) {

            preparedStatement.setInt(1, servicoId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}