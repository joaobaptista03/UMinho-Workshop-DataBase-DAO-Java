package src.data;

import src.business.*;

public interface OficinaDAOInterface {
    public void insertCliente(Cliente cliente);
    public void updateCliente(Cliente cliente);
    public void deleteCliente(int clienteId);
    public int getNrClientes();
    public boolean authenticateCliente(String clientEmail, String clientPassword);
}
