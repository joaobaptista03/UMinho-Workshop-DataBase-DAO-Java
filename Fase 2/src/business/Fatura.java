package src.business;

public class Fatura {
    private int nrFatura;
    private Cliente cliente;

    public Fatura(int nrFatura, Cliente cliente) {
        this.nrFatura = nrFatura;
        this.cliente = cliente;
    }

    public int getNrFatura() {
        return this.nrFatura;
    }

    public void setNrFatura(int nrFatura) {
        this.nrFatura = nrFatura;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente= cliente;
    }

    @Override
    public String toString() {
        return "Fatura{" +
                "nrFatura=" + nrFatura +
                ", cliente=" + cliente +
                '}';
    }
}
