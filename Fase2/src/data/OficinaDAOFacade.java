package src.data;

import java.time.LocalDate;
import java.util.List;

import src.business.*;

public interface OficinaDAOFacade {
    public boolean insertCliente(Cliente cliente);
    public void updateCliente(Cliente cliente);
    public void deleteCliente(int clienteId);
    public int getNrClientes();
    public int authenticateCliente(String clientEmail, String clientPassword);
    public Cliente getCliente(int clienteId);

    public boolean insertFuncionario(Funcionario funcionario);
    public void updateFuncionario(Funcionario funcionario);
    public void deleteFuncionario(int funcionarioID);
    public int getNrFuncionarios();
    public int authenticateFuncionario(String username, String password);
    public Funcionario getFuncionario(int funcionarioId);
    public List<FuncionarioTurno> getTurnosFuncionario(int funcionarioId);

    public void insertTurno(FuncionarioTurno turno);
    public void updateTurno(FuncionarioTurno turno);
    public void deleteTurno(int turnoID);
    public int getNrTurnos();
    public FuncionarioTurno getTurno(int turnoId);

    public void insertVeiculo(Veiculo veiculo);
    public void updateVeiculo(Veiculo veiculo);
    public void deleteVeiculo(String matricula);
    public int getNrVeiculos();
    public Veiculo getVeiculo(String matricula);

    public void insertFatura(Fatura fatura);
    public void updateFatura(Fatura fatura);
    public void deleteFatura(int faturaID);
    public int getNrFaturas();
    public Fatura getFatura(int faturaId);

    public void insertServico(Servico servico);
    public void updateServico(Servico servico);
    public void deleteServico(int servicoID);
    public int getNrServicos();
    public String consultarEstadoServico(int servicoId);
    public Servico getServico(int servicoId);
    public List<Servico> servicosEntreDatas(LocalDate dataInicio, LocalDate dataFim);
    
    public void insertAdministrador(Administrador Administrador);
    public void updateAdministrador(Administrador Administrador);
    public void deleteAdministrador(int administradorID);
    public int getNrAdministradores();
    public int authenticateAdministrador(String username, String password);
    public Administrador getAdministrador(int administradorId);
}
