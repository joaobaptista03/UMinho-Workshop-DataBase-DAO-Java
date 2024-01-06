package src;

import java.io.IOException;
import java.text.ParseException;

import src.ui.*;
import src.data.OficinaDAOFacade;
import src.data.OficinaDAOImpl;

public class Main {
    public static void main(String[] args) throws ParseException {
        try {
            OficinaDAOFacade oficinaDAO = new OficinaDAOImpl();
            AuthenticationUI.AuthTypeID auth = AuthenticationUI.authenticate(oficinaDAO);

            if (auth == null) {
                System.out.println("Autenticação falhada. A sair...");
                return;
            }

            System.out.println("Bem vindo/a à Oficina!\n");
            
            switch (auth.authType) {
                case 1:
                    new ClienteUI(oficinaDAO).execute();
                    break;
                case 2:
                    new FuncionarioUI(oficinaDAO, auth.id).execute();
                    break;
                case 3:
                    new AdministradorUI(oficinaDAO, auth.id).execute();
                    break;
                default:
                    System.out.println("Cargo inválido. A sair...");
            }

        } catch (IOException e) {
            System.out.println("Erro a iniciar a aplicação.: " + e.getMessage());
        }
    }
}
