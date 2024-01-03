package src.business;

public class Fatura {
    private int nrFatura;
    private Cliente cliente;
    private float preco;
    private boolean pago;

    public Fatura(int nrFatura, Cliente cliente, float preco, boolean pago) {
        this.nrFatura = nrFatura;
        this.cliente = cliente;
        this.preco = preco;
        this.pago = pago;
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

    public float getPreco() {
        return this.preco;
    }

    public void setPreco(float preco) {
        this.preco= preco;
    }

    public boolean getPago() {
        return this.pago;
    }

    public void setPago(boolean pago) {
        this.pago= pago;
    }

    @Override
    public String toString() {
        return "Fatura{" +
                "nrFatura=" + nrFatura +
                ", cliente=" + cliente +
                ", preco=" + preco +
                ", pago=" + pago +
                '}';
    }
}
