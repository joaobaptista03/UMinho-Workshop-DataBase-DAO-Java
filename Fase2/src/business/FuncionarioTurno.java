package src.business;

import java.time.LocalDateTime;
import java.util.Objects;

public class FuncionarioTurno {
    private int id;
    private int idFuncionario;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public FuncionarioTurno(int id, int idFuncionario, LocalDateTime inicio, LocalDateTime fim) {
        this.id = id;
        this.idFuncionario = idFuncionario;
        this.inicio = inicio;
        this.fim = fim;
    }

    public int getId() {
        return id;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public String toString() {
        return "Turno{" +
                "id=" + id +
                ", idFuncionario=" + idFuncionario +
                ", inicio=" + inicio +
                ", fim=" + fim +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuncionarioTurno)) return false;
        FuncionarioTurno turno = (FuncionarioTurno) o;
        return id == turno.id &&
                idFuncionario == turno.idFuncionario &&
                inicio.equals(turno.inicio) &&
                fim.equals(turno.fim);
    }

    public int hashCode() {
        return Objects.hash(id, idFuncionario, inicio, fim);
    }    
}