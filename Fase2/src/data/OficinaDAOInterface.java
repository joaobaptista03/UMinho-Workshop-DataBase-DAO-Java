package src.data;

import src.business.*;

public interface OficinaDAOInterface {
    public boolean insertCliente(Cliente cliente);
    public void updateCliente(Cliente cliente);
    public void deleteCliente(int clienteId);
    public int getNrClientes();
    public int authenticateCliente(String clientEmail, String clientPassword);

    public void insertFuncionario(Funcionario funcionario);
    public void updateFuncionario(Funcionario funcionario);
    public void deleteFuncionario(int funcionarioID);
    public int getNrFuncionarios();
    public int authenticateFuncionario(String username, String password);

    public void insertVeiculo(Veiculo veiculo);
    public void updateVeiculo(Veiculo veiculo);
    public void deleteVeiculo(String matricula);
    public int getNrVeiculos();

    public void insertFatura(Fatura fatura);
    public void updateFatura(Fatura fatura);
    public void deleteFatura(int faturaID);
    public int getNrFaturas();

    public void insertServico(Servico servico);
    public void updateServico(Servico servico);
    public void deleteServico(int servicoID);
    public int getNrServicos();
    public String consultarEstadoServico(int servicoId);
    
    public void insertAdministrador(Administrator administrator);
    public void updateAdministrador(Administrator administrator);
    public void deleteAdministrador(int administradorID);
    public int getNrAdministradores();
    public int authenticateAdministrator(String username, String password);
}
