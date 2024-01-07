package src.business;

import java.util.Objects;

import lib.BCrypt;

public class Administrator {
    private int id;
    private String nome;
    private String email;
    private String password;;

    public Administrator(int id, String nome, String email, String password) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
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


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nome='" + getNome() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Administrator)) {
            return false;
        }
        Administrator administrator = (Administrator) o;
        return id == administrator.id && Objects.equals(nome, administrator.nome) && Objects.equals(email, administrator.email) && Objects.equals(password, administrator.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, password);
    }
}