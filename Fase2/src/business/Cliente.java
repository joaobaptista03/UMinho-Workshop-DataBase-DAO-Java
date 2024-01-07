package src.business;

import lib.BCrypt;

public class Cliente {
    private int id;
    private String nome;
    private int nif;
    private String morada;
    private String email;
    private String password;
    private int telefone;

    public Cliente(int id, String nome, int nif, String morada, String email, String password, int telefone) {
        this.id = id;
        this.nome = nome;
        this.nif = nif;
        this.morada = morada;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id= id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome= nome;
    }

    public int getNif() {
        return this.nif;
    }

    public void setNif(int nif) {
        this.nif= nif;
    }

    public String getMorada() {
        return this.morada;
    }

    public void setMorada(String morada) {
        this.morada= morada;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password= password;
    }

    public int getTelefone() {
        return this.telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone= telefone;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome=" + nome +
                ", nif=" + nif +
                ", morada=" + morada +
                ", email=" + email +
                ", password=" + password +
                ", telefone=" + telefone +
                '}';
    }
}
