package src.business;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import lib.BCrypt;

public class Funcionario {
    private int id;
    private String nome;
    private String email;
    private String password;
    private int nrCartao;
    private String posto;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private List<FuncionarioTurno> livroDePonto;
    private List<String> competencias;
    private int administradorAdicionado;

    public Funcionario(int id, String nome, String email, String password, int nrCartao, String posto, LocalTime horaEntrada, LocalTime horaSaida, List<FuncionarioTurno> livroDePonto, List<String> competencias, int administradorAdicionado) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.nrCartao = nrCartao;
        this.posto = posto;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.livroDePonto = livroDePonto;
        this.competencias = competencias;
        this.administradorAdicionado = administradorAdicionado;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNrCartao() {
        return this.nrCartao;
    }

    public void setNrCartao(int nrCartao) {
        this.nrCartao = nrCartao;
    }

    public String getPosto() {
        return this.posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public LocalTime getHoraEntrada() {
        return this.horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSaida() {
        return this.horaSaida;
    }

    public void setHoraSaida(LocalTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public List<FuncionarioTurno> getLivroDePonto() {
        return this.livroDePonto;
    }

    public void setLivroDePonto(List<FuncionarioTurno> livroDePonto) {
        this.livroDePonto = livroDePonto;
    }

    public List<String> getCompetencias() {
        return this.competencias;
    }

    public void setCompetencias(List<String> competencias) {
        this.competencias = competencias;
    }

    public int getAdministradorAdicionado() {
        return this.administradorAdicionado;
    }

    public void setAdministradorAdicionado(int administradorAdicionado) {
        this.administradorAdicionado = administradorAdicionado;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Funcionario)) {
            return false;
        }
        Funcionario funcionario = (Funcionario) o;
        return id == funcionario.id && Objects.equals(nome, funcionario.nome) && Objects.equals(email, funcionario.email) && Objects.equals(password, funcionario.password) && nrCartao == funcionario.nrCartao && Objects.equals(posto, funcionario.posto) && Objects.equals(horaEntrada, funcionario.horaEntrada) && Objects.equals(horaSaida, funcionario.horaSaida) && Objects.equals(livroDePonto, funcionario.livroDePonto) && Objects.equals(competencias, funcionario.competencias) && administradorAdicionado == funcionario.administradorAdicionado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, password, nrCartao, posto, horaEntrada, horaSaida, livroDePonto, competencias, administradorAdicionado);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nome='" + getNome() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", nrCartao='" + getNrCartao() + "'" +
            ", posto='" + getPosto() + "'" +
            ", horaEntrada='" + getHoraEntrada() + "'" +
            ", horaSaida='" + getHoraSaida() + "'" +
            ", livroDePonto='" + getLivroDePonto() + "'" +
            ", competencias='" + getCompetencias() + "'" +
            ", administradorAdicionado='" + getAdministradorAdicionado() + "'" +
            "}";
    }
}