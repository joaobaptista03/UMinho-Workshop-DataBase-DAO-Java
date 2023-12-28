package src.business;

import java.util.List;
import java.util.Objects;

public class Funcionario {
    private int id;
    private String nome;
    private int nrCartao;
    private String posto;
    private List<String> competencias;

    public Funcionario(int id, String nome, int nrCartao, String posto, List<String> competencias) {
        this.id = id;
        this.nome = nome;
        this.nrCartao = nrCartao;
        this.posto = posto;
        this.competencias = competencias;
    }

    public int getId() {
        return id;
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

    public List<String> getCompetencias() {
        return this.competencias;
    }

    public void setCompetencias(List<String> competencias) {
        this.competencias = competencias;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Funcionario)) {
            return false;
        }
        Funcionario funcionario = (Funcionario) o;
        return id == funcionario.id && Objects.equals(nome, funcionario.nome) && nrCartao == funcionario.nrCartao && Objects.equals(posto, funcionario.posto) && Objects.equals(competencias, funcionario.competencias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, nrCartao, posto, competencias);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nome='" + getNome() + "'" +
            ", nrCartao='" + getNrCartao() + "'" +
            ", posto='" + getPosto() + "'" +
            ", competencias='" + getCompetencias() + "'" +
            "}";
    }
    
}
