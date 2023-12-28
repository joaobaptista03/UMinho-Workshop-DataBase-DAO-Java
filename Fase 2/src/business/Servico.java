package src.business;

import java.util.Date;

public class Servico {
    private int id;
    private String estado;
    private Date dataHora;
    private Funcionario funcionario;
    private Fatura fatura;
    private Veiculo Veiculo;
    private ServicoTipo servicoTipo;

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

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
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
        return Veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        Veiculo = veiculo;
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
                ", Veiculo=" + Veiculo +
                ", servicoTipo=" + servicoTipo +
                '}';
    }
}
