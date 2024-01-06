package src.business;

import java.time.LocalDateTime;

public class Servico {
    private int id;
    private String estado;
    private LocalDateTime dataHora;
    private Funcionario funcionario;
    private Fatura fatura;
    private Veiculo veiculo;
    private ServicoTipo servicoTipo;

    public Servico(int id, String estado, LocalDateTime dataHora, Funcionario funcionario, Fatura fatura, Veiculo veiculo, ServicoTipo servicoTipo) {
        this.id = id;
        this.estado = estado;
        this.dataHora = dataHora;
        this.funcionario = funcionario;
        this.fatura = fatura;
        this.veiculo = veiculo;
        this.servicoTipo = servicoTipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Fatura getFatura() {
        return fatura;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public ServicoTipo getServicoTipo() {
        return servicoTipo;
    }

    public void setServicoTipo(ServicoTipo servicoTipo) {
        this.servicoTipo = servicoTipo;
    }

    @Override
    public String toString() {
        return "Servico{" +
                "id=" + id +
                ", estado='" + estado + '\'' +
                ", dataHora=" + dataHora +
                ", funcionario=" + funcionario +
                ", fatura=" + fatura +
                ", Veiculo=" + veiculo +
                ", servicoTipo=" + servicoTipo +
                '}';
    }
}