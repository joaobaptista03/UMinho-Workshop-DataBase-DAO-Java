package src;

import java.io.IOException;

import src.ui.*;
import src.data.OficinaDAO;

public class Main {
    public static void main(String[] args) {
        try {
            OficinaDAO oficinaDAO = new OficinaDAO();

            int authType = AuthenticationUI.authenticate(oficinaDAO);

            if (authType == -1) {
                System.out.println("Autenticação falhada. A sair...");
                return;
            }

            System.out.println("Bem vindo/a à Oficina!\n");
            
            switch (authType) {
                case 1:
                    // Cliente
                    break;
                case 2:
                    new FuncionarioUI(oficinaDAO).execute();
                    break;
                case 3:
                    new AdministradorUI(oficinaDAO).execute();
                    break;
                default:
                    System.out.println("Cargo inválido. A sair...");
            }

        } catch (IOException e) {
            System.out.println("Erro a iniciar a aplicação.: " + e.getMessage());
        }
    }
}
