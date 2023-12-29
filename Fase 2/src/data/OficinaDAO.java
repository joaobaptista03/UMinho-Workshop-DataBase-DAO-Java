package src.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import src.business.*;

public class OficinaDAO implements OficinaDAOInterface{

    private static final String URL = DAOConfig.URL;
    private static final String URL_W_DATABASE = DAOConfig.URL + "OficinaDB";

    private static final String USERNAME = DAOConfig.USERNAME;
    private static final String PASSWORD = DAOConfig.PASSWORD;

    private Connection getConnection(boolean init) throws SQLException {
        if (init) return DriverManager.getConnection(URL_W_DATABASE, USERNAME, PASSWORD);
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public OficinaDAO() throws IOException {
        try (Connection connection = getConnection(false);) {
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
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM clientes WHERE id = ?")) {

            preparedStatement.setInt(1, clienteId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrClientes() {
        int nrClientes = 0;
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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

    public void insertFuncionario(Funcionario funcionario) {
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO funcionarios VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setInt(1, funcionario.getId());
            preparedStatement.setString(2, funcionario.getNome());
            preparedStatement.setString(3, funcionario.getEmail());
            preparedStatement.setString(4, funcionario.getPassword());
            preparedStatement.setInt(5, funcionario.getNrCartao());
            preparedStatement.setString(6, funcionario.getPosto());

            List<String> competencias = funcionario.getCompetencias();
            String competenciasString = competencias.stream().collect(Collectors.joining(","));
            preparedStatement.setString(7, competenciasString);

            preparedStatement.setInt(8, funcionario.getAdministradorAdicionado());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFuncionario(Funcionario funcionario) {
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE funcionarios SET nome = ?, nrCartao = ?, email = ?, password = ?, posto = ?, competencias = ?, administradorAdicionado = ? WHERE id = ?")) {

            preparedStatement.setString(1, funcionario.getNome());
            preparedStatement.setInt(2, funcionario.getNrCartao());
            preparedStatement.setString(3, funcionario.getEmail());
            preparedStatement.setString(4, funcionario.getPassword());
            preparedStatement.setString(5, funcionario.getPosto());

            List<String> competencias = funcionario.getCompetencias();
            String competenciasString = competencias.stream().collect(Collectors.joining(","));
            preparedStatement.setString(6, competenciasString);

            preparedStatement.setInt(7, funcionario.getId());
            preparedStatement.setInt(8, funcionario.getAdministradorAdicionado());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFuncionario(int funcionarioId) {
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM funcionarios WHERE id = ?")) {

            preparedStatement.setInt(1, funcionarioId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrFuncionarios() {
        int nrFuncionarios = 0;
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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

    public void insertVeiculo(Veiculo veiculo) {
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM veiculos WHERE matricula = ?")) {

            preparedStatement.setString(1, matricula);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrVeiculos() {
        int nrVeiculos = 0;
        try (Connection connection = getConnection(true);
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

    public void insertFatura(Fatura fatura) {
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO faturas VALUES (?, ?)")) {

            preparedStatement.setInt(1, fatura.getNrFatura());
            preparedStatement.setInt(2, fatura.getCliente().getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFatura(Fatura fatura) {
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE faturas SET clienteId = ? WHERE nrFatura = ?")) {

            preparedStatement.setInt(1, fatura.getCliente().getId());
            preparedStatement.setInt(2, fatura.getNrFatura());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFatura(int nrFatura) {
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM faturas WHERE nrFatura = ?")) {

            preparedStatement.setInt(1, nrFatura);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrFaturas() {
        int nrFaturas = 0;
        try (Connection connection = getConnection(true);
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
    
    public void insertServico(Servico servico) {
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM servicos WHERE id = ?")) {

            preparedStatement.setInt(1, servicoId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNrServicos() {
        int nrServicos = 0;
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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

    public void insertAdministrador(Administrator administrator) {
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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
        try (Connection connection = getConnection(true);
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
}